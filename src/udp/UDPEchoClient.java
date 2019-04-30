package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEchoClient {

	public static final String SERVER_IP = "192.168.1.27";
	public static final int SERVER_PORT = 7000;
	
	public static void main(String[] args) {

		Scanner sc = null;
		DatagramSocket socket = null;
		
		try {
			//1) 소켓 생성 및 입력
			sc = new Scanner(System.in);
			socket = new DatagramSocket();
			
			while(true) {
				//2) 키보드 입력
				System.out.print(">>");
				String line = sc.nextLine();
				if("quit".equals(line)) {
					break;
				}
				//3) 데이터 쓰기(클 -> 서)
				byte [] sendData = line.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(SERVER_IP, UDPEchoServer.PORT));
				socket.send(sendPacket);
				
				byte [] buf = new byte[UDPEchoServer.BUFFER_SIZE];
				//4) 데이터 읽기(서 -> 클)
				DatagramPacket receivePacket = new DatagramPacket(buf, UDPEchoServer.BUFFER_SIZE);
				socket.receive(receivePacket);
				
				byte [] receiveData = receivePacket.getData();
				int len = receivePacket.getLength();
				String message = new String(receiveData,0,len,"utf-8");
				
				//5) 콘솔 출력
				System.out.println("<<"+message);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		//클라이언트 종료
		finally {
			if(sc!=null) {
				sc.close();
			}
			if(!socket.isClosed()&&socket!=null) {
				socket.close();	
			}
		}
	}
	//진행상황 표시할 로그
	public static void log(String log) {
		System.out.println("[client] "+log);
	}
}
