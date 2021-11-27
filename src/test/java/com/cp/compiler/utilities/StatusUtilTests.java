package com.cp.compiler.utilities;

import com.cp.compiler.models.Verdict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The type Status util tests.
 */
class StatusUtilTests {
	/**
	 * Should return accepted.
	 */
	@Test
	void shouldReturnAccepted() {
		// When
		String status = StatusUtil.statusResponse(0, true);
		
		// Then
		Assertions.assertEquals(Verdict.ACCEPTED.getValue(), status);
	}
	
	/**
	 * Should return wrong answer.
	 */
	@Test
	void shouldReturnWrongAnswer() {
		// When
		String status = StatusUtil.statusResponse(0, false);
		
		// Then
		Assertions.assertEquals(Verdict.WRONG_ANSWER.getValue(), status);
	}
	
	/**
	 * Should return run time error.
	 */
	@Test
	void shouldReturnRunTimeError() {
		// When
		String status = StatusUtil.statusResponse(1, false);
		
		// Then
		Assertions.assertEquals(Verdict.RUNTIME_ERROR.getValue(), status);
	}
	
	/**
	 * Should return compilation error.
	 */
	@Test
	void shouldReturnCompilationError() {
		// When
		String status = StatusUtil.statusResponse(2, false);
		
		// Then
		Assertions.assertEquals(Verdict.COMPILATION_ERROR.getValue(), status);
	}
	
	/**
	 * Should return out of memory.
	 */
	@Test
	void shouldReturnOutOfMemory() {
		// WHen
		String status = StatusUtil.statusResponse(139, false);
		
		// Then
		Assertions.assertEquals(Verdict.OUT_OF_MEMORY.getValue(), status);
	}
	
	/**
	 * Should return time limit exceeded.
	 */
	@Test
	void shouldReturnTimeLimitExceeded() {
		// WHen
		String status = StatusUtil.statusResponse(124, false);
		
		// Then
		Assertions.assertEquals(Verdict.TIME_LIMIT_EXCEEDED.getValue(), status);
	}
}
