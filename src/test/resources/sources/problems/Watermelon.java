import java.io.*;
import java.util.*;

// From Codeforces
public class Watermelon {
	private void run() throws IOException {
		int n = in.nextInt();
		if (n >= 4 && (n - 2) % 2 == 0) {
			out.println("YES");
		} else {
			out.println("NO");
		}
		out.flush();
	}
	
	private class Scanner {
		private StringTokenizer tokenizer;
		private BufferedReader reader;
		
		public Scanner(Reader in) {
			reader = new BufferedReader(in);
			tokenizer = new StringTokenizer("");
		}
		
		public boolean hasNext() throws IOException {
			while (!tokenizer.hasMoreTokens()) {
				String next = reader.readLine();
				if (next == null)
					return false;
				tokenizer = new StringTokenizer(next);
			}
			return true;
		}
		
		public String next() throws IOException {
			hasNext();
			return tokenizer.nextToken();
		}
		
		public String nextLine() throws IOException {
			tokenizer = new StringTokenizer("");
			return reader.readLine();
		}
		
		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Watermelon().run();
	}
	Scanner in = new Scanner(new InputStreamReader(System.in));
	PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
}