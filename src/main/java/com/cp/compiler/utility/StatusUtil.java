package com.cp.compiler.utility;

public class StatusUtil {
	
	private StatusUtil() {
	}
	
	// Return the verdict
	public static String statusResponse(int status, boolean ans) {
		
		// the status is taken from the return code of the container
		switch (status) {
			
			// The code compile and the execution finish before the time limit, and the memory does not exceed the limit
			case 0:
				// Is it the excepted output ?
				if (ans)
					return "Accepted";
				else
					return "Wrong Answer";
			
			case 1:
				return "Runtime Error";
			
			case 2:
				return "Compilation Error";
			
			case 139:
				return "Out Of Memory";
			
			default:
				return "Time Limit Exceeded";
		}
	}
}
