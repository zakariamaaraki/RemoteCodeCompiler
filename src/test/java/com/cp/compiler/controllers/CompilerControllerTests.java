package com.cp.compiler.controllers;

import com.cp.compiler.models.Language;
import com.cp.compiler.models.Response;
import com.cp.compiler.services.CompilerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * The type Compiler controller tests.
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CompilerControllerTests {
	
	@InjectMocks
	private CompilerController compilerController;
	@Mock
	private CompilerService compilerService;
	@Mock
	private MultipartFile outputFile;
	@Mock
	private MultipartFile sourceCode;
	
	/**
	 * When compiling java code should return a response object in the body.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void whenCompilingJavaCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Language.JAVA))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compileJava(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
	/**
	 * When compiling c code should return a response object in the body.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void whenCompilingCCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Language.C))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compileC(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
	/**
	 * When compiling cpp code should return a response object in the body.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void whenCompilingCppCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Language.CPP))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compileCpp(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
	/**
	 * When compiling python code should return a response object in the body.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void whenCompilingPythonCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Language.PYTHON))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compilePython(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
}
