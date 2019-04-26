package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookUp {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		while(true) {
			try {
				String site = sc.next();
				if(site.equals("exit")) break;
				
				InetAddress[] allAddress = InetAddress.getAllByName(site);
				
				for(InetAddress ia : allAddress) {
					System.out.println(ia.getHostName());
					System.out.println(ia.getHostAddress());
				}
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}
}
