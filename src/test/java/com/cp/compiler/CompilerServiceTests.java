package com.cp.compiler;

import com.cp.compiler.model.Languages;
import com.cp.compiler.service.CompilerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class CompilerServiceTests {
	
	@Autowired
	private CompilerService compilerService;
	
	private static final int BAD_REQUEST = 400;
	
	@Test
	public void WhenTimeLimitGreaterThan15ShouldReturnBadRequest() throws Exception {
		// Given
		int timeLimit = 16;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, timeLimit, 500, Languages.Java);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	@Test
	public void WhenTimeLimitLessThan0ShouldReturnBadRequest() throws Exception {
		// Given
		int timeLimit = -1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, timeLimit, 500, Languages.Java);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	@Test
	public void WhenMemoryLimitGreaterThan1000ShouldReturnBadRequest() throws Exception {
		// Given
		int memoryLimit = 1001;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, 0, memoryLimit, Languages.Java);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	@Test
	public void WhenMemoryLimitLessThan0ShouldReturnBadRequest() throws Exception {
		// Given
		int memoryLimit = -1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, 0, memoryLimit, Languages.Java);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
}
