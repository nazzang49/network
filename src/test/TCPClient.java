package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.omg.CORBA.portable.UnknownException;

//클라이언트 단
public class TCPClient {

	//상수 선언(대문자 변수명 지정) = 접속할 서버 소켓 정보
	private static final String SERVER_IP = "192.168.1.27";
	private static final int SERVER_PORT = 6000;
	
	public static void main(String[] args) throws UnknownException {

		Socket client = null;
		
		try {
			//1) 소켓 생성
			client = new Socket();
			
			//2) 서버 연결
			try {
				client.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
				System.out.println("Client is connected.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//3) IOStream 받기
			InputStream is = client.getInputStream();
			OutputStream os = client.getOutputStream();
			
			//4) 쓰기
			String data = "Hello World\n";
			os.write(data.getBytes("utf-8"));
			
			//5) 읽기
			byte[] buf = new byte[256];
			int readByCount = is.read(buf);
			//더 이상 읽을 데이터가 없으면
			if(readByCount==-1) {
				System.out.println("Client is closed by server.");
			}
			data = new String(buf,0,readByCount,"utf-8");
			System.out.println("FROM 서버"+data);
		}catch(IOException e) {
			e.printStackTrace();
		}
		//클라이언트 종료
		finally {
			try {
				if(!client.isClosed()&&client!=null) {
					client.close();	
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
