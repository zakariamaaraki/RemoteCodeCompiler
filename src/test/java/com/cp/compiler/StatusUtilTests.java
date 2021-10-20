package com.cp.compiler;

import com.cp.compiler.utility.StatusUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatusUtilTests {
	
	@Test
	public void shouldReturnAccepted() {
		// When
		String status = StatusUtil.statusResponse(0, true);
		
		// Then
		Assertions.assertEquals("Accepted", status);
	}
	
	@Test
	public void shouldReturnWrongAnswer() {
		// When
		String status = StatusUtil.statusResponse(0, false);
		
		// Then
		Assertions.assertEquals("Wrong Answer", status);
	}
	
	@Test
	public void shouldReturnRunTimeError() {
		// When
		String status = StatusUtil.statusResponse(1, false);
		
		// Then
		Assertions.assertEquals("Runtime Error", status);
	}
	
	@Test
	public void shouldReturnCompilationError() {
		// When
		String status = StatusUtil.statusResponse(2, false);
		
		// Then
		Assertions.assertEquals("Compilation Error", status);
	}
	
	@Test
	public void shouldReturnOutOfMemory() {
		// WHen
		String status = StatusUtil.statusResponse(139, false);
		
		// Then
		Assertions.assertEquals("Out Of Memory", status);
	}
	
	@Test
	public void shouldReturnTimeLimitExceeded() {
		// WHen
		String status = StatusUtil.statusResponse(257, false);
		
		// Then
		Assertions.assertEquals("Time Limit Exceeded", status);
	}
}
