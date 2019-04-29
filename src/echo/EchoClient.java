package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import org.omg.CORBA.portable.UnknownException;

public class EchoClient {

	//상수 선언(대문자 변수명 지정) = 접속할 서버 소켓 정보
	private static final String SERVER_IP = "192.168.1.27";
	private static final int SERVER_PORT = 7000;
	
	public static void main(String[] args) throws UnknownException {

		Scanner sc = null;
		Socket client = null;
		
		try {
			//1) 소켓 생성 및 입력
			sc = new Scanner(System.in);
			client = new Socket();
			
			//2) 서버 연결
			try {
				client.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
				log("connected");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//3) IOStream 받기
			InputStream is = client.getInputStream();
			OutputStream os = client.getOutputStream();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(),"utf-8"));
			//true = auto flush(자동으로 데이터 다 차지 않아도 전송)
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream(),"utf-8"),true);
			
			while(true) {
				//4) 키보드 입력
				System.out.print(">>");
				String line = sc.nextLine();
				if("quit".equals(line)) {
					break;
				}
				//5) 데이터 쓰기(클 -> 서)
				pw.println(line);
				
				//6) 데이터 읽기(서 -> 클)
				String data = br.readLine();
				if(data==null) {
					log("closed by server");
					break;
				}
				
				//7) 콘솔 출력
				System.out.print("<<"+data);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		//클라이언트 종료
		finally {
			try {
				if(sc!=null) {
					sc.close();
				}
				if(!client.isClosed()&&client!=null) {
					client.close();	
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	//진행상황 표시할 로그
	public static void log(String log) {
		System.out.println("[client] "+log);
	}
}
