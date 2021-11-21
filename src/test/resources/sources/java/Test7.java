public class Test7 {
	
	public static void main(String[] args) throws Exception {
		
		int i = 0;
		
		while (i < 10) {
			System.out.println(i++);
			Thread.sleep(1000);
		}
	}
	
}