package com.cp.compiler;

import com.cp.compiler.controller.CompilerController;
import com.cp.compiler.model.Languages;
import com.cp.compiler.model.Response;
import com.cp.compiler.service.CompilerService;
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

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CompilerControllerTests {
	
	@InjectMocks
	CompilerController compilerController;
	@Mock
	private CompilerService compilerService;
	@Mock
	private MultipartFile outputFile;
	@Mock
	private MultipartFile sourceCode;
	
	@Test
	public void whenCompilingJavaCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Languages.Java))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compile_java(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
	@Test
	public void whenCompilingCCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Languages.C))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compile_c(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
	@Test
	public void whenCompilingCppCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Languages.Cpp))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compile_cpp(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
	@Test
	public void whenCompilingPythonCodeShouldReturnAResponseObjectInTheBody() throws Exception {
		// Given
		Mockito.when(compilerService.compile(outputFile, sourceCode, null, 10, 500, Languages.Python))
				.thenReturn(ResponseEntity
						.status(HttpStatus.OK)
						.body(new Response("test output", "test expected output", "Accepted", LocalDateTime.now())));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compile_python(outputFile, sourceCode, null, 10, 500);
		
		// Then
		Assertions.assertThat(responseEntity.getBody() instanceof Response);
	}
	
}
