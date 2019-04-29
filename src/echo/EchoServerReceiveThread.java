package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread{

	private Socket client;
	
	public EchoServerReceiveThread(Socket client) {
		this.client=client;
	}
	
	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)client.getRemoteSocketAddress();
		String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
		int remotePort = inetRemoteSocketAddress.getPort();
		EchoServer.log("connected by client["+remoteHostAddress+":"+remotePort+"]");
		
		try {
			BufferedReader br = new BufferedReader( 
					new InputStreamReader(client.getInputStream(),"utf-8") ); 				
			PrintWriter pr = new PrintWriter( 
					new OutputStreamWriter(client.getOutputStream(),"utf-8"),true);
			while(true) {
				String data = br.readLine();
				if(data == null) {
					EchoServer.log("closed by client");
					break;
				}
				EchoServer.log("received --> " + data);
				pr.println(data);
			}
		}catch(SocketException e) {
			System.out.println("[server] sudden closed by client");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(client != null && client.isClosed() == false ) {
					client.close();
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}	
	}
}
