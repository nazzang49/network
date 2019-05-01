package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;

//클라이언트
public class RequestHandler extends Thread {
	private static String documentRoot = "";
	
	//static 영역
	static{
		//class path를 절대경로로 바꾸는 작업
		documentRoot = RequestHandler.class.getClass().getResource("/webapp").getPath();
	}
	
	private Socket socket;
	
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}
	
	//각 스레드마다 진행되는 동일한 규칙
	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port (접속된 서버 소켓 정보 확인)
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
			
			// get IOStream
			OutputStream os = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			
			String request = null;
			
			while(true) {
				String line = br.readLine();
				//브라우저가 연결을 끊을 시
				if(line==null) {
					break;
				}
				//Request Header 정보만 읽어오기 "" = body가 시작되는 부분
				if("".equals(line)) {
					break;
				}
				//Header의 첫번째 개행만 추출
				if(request==null) {
					request = line;
				}				
			}
			String [] tokens = request.split(" ");
			if("GET".contentEquals(tokens[0])) {
				consoleLog("request --> "+tokens[1]);
				responseStaticResource(os, tokens[1], tokens[2]);
			}
			//POST, GET, PUT, DELTE, HEAD, CONNECT의 경우 무시
			else {
				response400Error(os, tokens[2]);
				String url = "/error/400.html";
				File file = new File(documentRoot+url);
				byte [] body = Files.readAllBytes(file.toPath());
				os.write(body);
				return;
			}
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	//GET(REST 명령어) /(URL이 기록되는 부분, 기본 = /) HTTP/1.1(프로토콜)
	public void responseStaticResource(OutputStream os, String url, String protocol) throws IOException{
		//호출할 페이지를 명시하지 않은 경우 디폴트 페이지 설정
		if("/".equals(url)) {
			url = "/index.html";
		}
		File file = new File(documentRoot+url);
		
		//페이지가 없는 경우(404)
		if(!file.exists()) {
			response404Error(os,protocol);
			url = "/error/404.html";
			file = new File(documentRoot+url);
			byte [] body = Files.readAllBytes(file.toPath());
			os.write(body);
			return;
		}
		//페이지가 있는 경우(200)
		byte [] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		//응답
		os.write((protocol+" 200 OK\r\n").getBytes( "UTF-8" ) );
		// body 태그 시작하기 전 정보 명시
		os.write(("Content-Type:"+contentType+"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
		os.write(body);
	}
	
	//페이지 부재
	public void response404Error(OutputStream os, String protocol) throws IOException {
		os.write((protocol+" 404 File Not Found\r\n").getBytes( "UTF-8" ) );
		os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
	}
	
	//잘못된 요청
	public void response400Error(OutputStream os, String protocol) throws IOException {
		os.write((protocol+" 400 Bad Request\r\n").getBytes( "UTF-8" ) );
		os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
	}	
	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
