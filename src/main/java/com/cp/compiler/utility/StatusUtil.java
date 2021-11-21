package com.cp.compiler.utility;

public class StatusUtil {
	
	private StatusUtil() {
	}
	
	/**
	 *
	 * @param status an integer that represents the status returned by the docker container
	 * @param ans if the status code is 0, then this boolean must be equal to true or false to specify if Response is Accepted, or it's a Wrong answer
	 * @return return a String representing the status response
	 */
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
			
			case 2: return "Compilation Error";
			
			case 139: return "Out Of Memory";
			
			case 124: return "Time Limit Exceeded";
			
			default: return "Runtime Error";
		}
	}
}
