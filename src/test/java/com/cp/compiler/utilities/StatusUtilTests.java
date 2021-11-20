package com.cp.compiler.utilities;

import com.cp.compiler.utility.StatusUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The type Status util tests.
 */
class StatusUtilTests {
	
	private static final String ACCEPTED_VERDICT = "Accepted";
	private static final String WRONG_ANSWER_VERDICT = "Wrong Answer";
	private static final String TIME_LIMIT_EXCEEDED_VERDICT = "Time Limit Exceeded";
	private static final String RUNTIME_ERROR_VERDICT = "Runtime Error";
	private static final String OUT_OF_MEMORY_ERROR_VERDICT = "Out Of Memory";
	private static final String COMPILATION_ERROR_VERDICT = "Compilation Error";
	
	/**
	 * Should return accepted.
	 */
	@Test
	void shouldReturnAccepted() {
		// When
		String status = StatusUtil.statusResponse(0, true);
		
		// Then
		Assertions.assertEquals(ACCEPTED_VERDICT, status);
	}
	
	/**
	 * Should return wrong answer.
	 */
	@Test
	void shouldReturnWrongAnswer() {
		// When
		String status = StatusUtil.statusResponse(0, false);
		
		// Then
		Assertions.assertEquals(WRONG_ANSWER_VERDICT, status);
	}
	
	/**
	 * Should return run time error.
	 */
	@Test
	void shouldReturnRunTimeError() {
		// When
		String status = StatusUtil.statusResponse(1, false);
		
		// Then
		Assertions.assertEquals(RUNTIME_ERROR_VERDICT, status);
	}
	
	/**
	 * Should return compilation error.
	 */
	@Test
	void shouldReturnCompilationError() {
		// When
		String status = StatusUtil.statusResponse(2, false);
		
		// Then
		Assertions.assertEquals(COMPILATION_ERROR_VERDICT, status);
	}
	
	/**
	 * Should return out of memory.
	 */
	@Test
	void shouldReturnOutOfMemory() {
		// WHen
		String status = StatusUtil.statusResponse(139, false);
		
		// Then
		Assertions.assertEquals(OUT_OF_MEMORY_ERROR_VERDICT, status);
	}
	
	/**
	 * Should return time limit exceeded.
	 */
	@Test
	void shouldReturnTimeLimitExceeded() {
		// WHen
		String status = StatusUtil.statusResponse(257, false);
		
		// Then
		Assertions.assertEquals(TIME_LIMIT_EXCEEDED_VERDICT, status);
	}
}
