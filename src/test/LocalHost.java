package test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalHost {

	public static List<String> hostIPAddresses() throws SocketException {
		List<String> result = new ArrayList<String>();
		for(NetworkInterface ifc : Collections.list(NetworkInterface.getNetworkInterfaces())) {
			//비활성화 NIC 무시
			if (ifc.isUp() == false) {
				continue;
			}
			//127.0.0.1 무시
			if (ifc.isLoopback() == true) {
				continue;
			}
			for (InetAddress inetAddr : Collections.list(ifc.getInetAddresses())) {
				//MAC Adress 무시
				if (inetAddr.isLinkLocalAddress() == true) continue;
				result.add(inetAddr.getHostAddress());
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			//기본 호스트 이름 및 주소
			String hostname = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
					
			System.out.println("[기본IP]");
			//기본 호스트 주소 
			byte [] addresses = inetAddress.getAddress();
			int idx = 0;
			for(byte address : addresses) {
				System.out.print(address&0x000000ff);
				if(idx!=3) System.out.print('.');
				idx++;
			}
			System.out.println();
			System.out.println("[활성화된 NIC]");
			//모든 호스트 중 활성화된 NIC만 추출하는 과정
			for(String IPAddress : hostIPAddresses()) {
				System.out.println(IPAddress);
			}
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
