package thread;

public class UppercaseAlphabet {

	//해당 메소드를 하나의 스레드로 변경
	public void print() {
		for(char c='A';c<='Z';c++) {
			System.out.print(c);
		}
	}
}
