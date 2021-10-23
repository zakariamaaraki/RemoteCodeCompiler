package com.cp.compiler;

import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.model.Languages;
import com.cp.compiler.model.Response;
import com.cp.compiler.model.Result;
import com.cp.compiler.service.CompilerService;
import com.cp.compiler.service.ContainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
public class CompilerServiceTests {
	
	@MockBean
	private ContainService containService;
	
	@Autowired
	private CompilerService compilerService;
	
	private static final int BAD_REQUEST = 400;
	
	private static final String ACCEPTED_VERDICT = "Accepted";
	private static final String WRONG_ANSWER_VERDICT = "Wrong Answer";
	
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
	
	@Test
	public void WhenImageBuildFailedShouldThrowDockerBuildException() {
		// Given
		Mockito.when(containService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new DockerBuildException("Error Building image"));
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// Then
		Assertions.assertThrows(DockerBuildException.class, () -> {
			// When
			compilerService.compile(file, file, null, 10, 100, Languages.Java);
		});
	}
	
	@Test
	public void WhenImageBuildSucceedShouldReturnAResult() throws Exception {
		// Given
		Mockito.when(containService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(ACCEPTED_VERDICT, "", "");
		
		Mockito.when(containService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.Java);
		
		// Then
		Assertions.assertEquals(ResponseEntity
				.status(HttpStatus.OK)
				.body(new Response(result.getOutput(), result.getExpectedOutput(), result.getVerdict(), null)).getStatusCode(), responseEntity.getStatusCode());
	}
	
	@Test
	public void WhenItsACorrectAnswerCompileMethodShouldReturnAcceptedVerdict() throws Exception {
		// Given
		Mockito.when(containService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(ACCEPTED_VERDICT, "", "");
		
		Mockito.when(containService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.Java);
		Response response = (Response) responseEntity.getBody();
		// Then
		Assertions.assertEquals(ACCEPTED_VERDICT, response.getStatus());
	}
	
	@Test
	public void WhenItsAWrongAnswerCompileMethodShouldReturnWrongAnswerVerdictVerdict() throws Exception {
		// Given
		Mockito.when(containService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(WRONG_ANSWER_VERDICT, "", "");
		
		Mockito.when(containService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.Java);
		Response response = (Response) responseEntity.getBody();
		// Then
		Assertions.assertEquals(WRONG_ANSWER_VERDICT, response.getStatus());
	}
}
