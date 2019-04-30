package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

//UDP는 단일 소켓으로 즉시 통신
public class UDPEchoServer {

	public static final int PORT = 7000;
	public static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {
		DatagramSocket socket = null;
		try {
			//1) 소켓 생성
			socket = new DatagramSocket(PORT);
			
			while(true) {
				byte [] buf = new byte[BUFFER_SIZE];
				//2) 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(buf, BUFFER_SIZE);
				socket.receive(receivePacket);
				
				byte [] receiveData = receivePacket.getData();
				int len = receivePacket.getLength();
				String message = new String(receiveData,0,len,"utf-8");
				
				System.out.println("[server] received : "+message);
				
				//3) 데이터 송신
				byte [] sendData = message.getBytes("utf-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						receivePacket.getAddress(), receivePacket.getPort());
				socket.send(sendPacket);
			}
		}catch(SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(socket!=null&&!socket.isClosed()) {
				socket.close();
			}
		}	
	}
}