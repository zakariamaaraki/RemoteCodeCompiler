public class Test3 {
	// Compilation Error
	public static void main(String[] args) {
		
		// The variable i is not declared
		i = 0;
		
		while (i < 10) {
			System.out.println(i++);
		}
	}
	
}