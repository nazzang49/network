package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class IOTest {

	public static void main(String[] args) throws IOException{

		//전화번호 기록부
		HashMap<String, String> map = new HashMap<>();
		
		//(방법1) 파일에 대한 메타데이터(정보)를 얻는 객체
		File file = new File("C:/cafe24/eclipse-workspace/network/test.txt");
		//(방법2) 스캐너 객체로 읽어들이는 방식(hasNextLine 메소드로 개행 정보 확인 가능)
		//프로젝트 바로 하단에 파일 생성 = 파일 이름만 적어주면 상대경로로 접근 가능
		Scanner sc = new Scanner(new File("test.txt"));
		
		//파일을 읽어들이는 리더 객체
		FileReader fr = new FileReader(file);
		//바이트에서 문자 단위로 변환하여 읽어들이는 리더 객체
		BufferedReader br = new BufferedReader(fr);
		
		String line = "";
		
		//읽어들일 정보가 남아있는 경우에만 진행
		while((line=br.readLine())!=null) {
			//(방법1) split 메소드
			String [] phone = line.split(" ");
			//각 인원별 전화번호 저장
			map.put(phone[0], phone[1]+"-"+phone[2]+"-"+phone[3]);
			
			//(방법2) StringTokenizer
			StringTokenizer st = new StringTokenizer(line," ");
			while(st.hasMoreTokens()) {
				String tmp = st.nextToken();
			}
		}
		//전화번호 검색을 위한 키보드 연결
		br = new BufferedReader(new InputStreamReader(System.in));
		//전화번호 검색
		String search = br.readLine();
		//전화번호가 등록된 사람인지 확인
		if(map.containsKey(search)) {
			System.out.println(map.get(search));
		}else {
			System.out.println("찾는 사람의 전화번호가 아직 없습니다.");
		}
		//스트림 종료
		br.close();
	}
}