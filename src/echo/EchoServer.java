package echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {

	private static final int SERVER_PORT = 7000;
	
	public static void main(String[] args) throws SocketException, IOException {

		//1) 서버 소켓 생성
		ServerSocket ss = new ServerSocket();
		
		//2) 바인딩 작업(IP+port)
		ss.bind(new InetSocketAddress("0.0.0.0",SERVER_PORT));
		log("server starts at the port --> "+SERVER_PORT);
		
		//3) 어셉트 작업
		while(true) {
			Socket client = ss.accept();
			Thread thread = new EchoServerReceiveThread(client);
			thread.start();	
		}
	}
	//진행상황 표시할 로그
	public static void log(String log) {
		System.out.println("[server#] "+Thread.currentThread().getName()+log);
	}
}