package thread;

public class ThreadEx02 {

	public static void main(String[] args) {

		//독립된 하나의 작업 경로를 만드는 과정
		Thread thread01 = new DigitThread();
		Thread thread02 = new AlphabetThread();
		Thread thread03 = new DigitThread();
		thread01.start();
		thread02.start();
		thread03.start();
	}
}
