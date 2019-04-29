package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

//서버 단
public class TCPServer {

	public static void main(String[] args) throws SocketException, IOException {

		//1) 서버 소켓 생성
		ServerSocket ss = new ServerSocket();
		
		//2) 바인딩 작업(IP+port)
		ss.bind(new InetSocketAddress("0.0.0.0",6000));
		
		//3) 어셉트 작업
		Socket client = ss.accept();
		//클라이언트 정보(IP+port) 출력
		InetSocketAddress isa = (InetSocketAddress)client.getRemoteSocketAddress();
		System.out.println("Server is connected by clinet : "+isa.getAddress().getHostAddress()+", "+isa.getPort());
		
		InputStream is = null;
		OutputStream os = null;
		
		try {
		//4) 데이터 통신 준비
		is = client.getInputStream(); //서버 -> 클라이언트
		os = client.getOutputStream(); //클라이언트 -> 서버
		//5) 데이터 송신
		while(true) {
		
				byte[] buf = new byte[256];
				int flag = is.read(buf);
				//더 이상 받을 데이터가 없으면
				if(flag==-1) {
					System.out.println("Server is closed by client.");
					break;
				}
				String data = new String(buf,0,flag,"utf-8");
				System.out.println("FROM 클라이언트 : "+data);
				
				//에코 확인
				os.write(data.getBytes("utf-8"));	
			
		}
		}catch(SocketException e) {
			System.out.println("Server is closed by sudden clause.");
			e.printStackTrace();
		}finally {
			//6) 연결 종료
			if(!ss.isClosed()&&ss!=null) {
				ss.close();
				is.close();
				os.close();
				client.close();
			}	
		}	
	}
}
