package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {

	public static final int PORT = 8200;
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
								
				//공백 입력에만 데이터 반환
				if("".equals(message)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
					String data = format.format(new Date());
					byte [] sendData = data.getBytes("utf-8");
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
							receivePacket.getAddress(), receivePacket.getPort());
					socket.send(sendPacket);
				}else {
					String data = "입력값 양식을 확인해주세요.";
					byte [] sendData = data.getBytes("utf-8");
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
							receivePacket.getAddress(), receivePacket.getPort());
					socket.send(sendPacket);
				}
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
