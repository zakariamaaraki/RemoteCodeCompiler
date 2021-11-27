package com.cp.compiler.services;

import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Result;
import com.cp.compiler.models.Verdict;
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

/**
 * The type Compiler service tests.
 */
@SpringBootTest
class CompilerServiceTests {
	
	private static final int BAD_REQUEST = 400;
	
	@MockBean
	private ContainerService containerService;
	@Autowired
	private CompilerService compilerService;
	
	/**
	 * When time limit greater than 15 should return bad request.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenTimeLimitGreaterThanMaxExecutionTimeShouldReturnBadRequest() throws Exception {
		// Given
		int timeLimit = compilerService.getMaxExecutionTime() + 1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, timeLimit, 500, Language.JAVA);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	/**
	 * When time limit less than 0 should return bad request.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenTimeLimitLessThanMinExecutionTimeShouldReturnBadRequest() throws Exception {
		// Given
		int timeLimit = compilerService.getMinExecutionTime() - 1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, timeLimit, 500, Language.JAVA);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	/**
	 * When memory limit greater than 1000 should return bad request.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenMemoryLimitGreaterThanMaxExecutionMemoryShouldReturnBadRequest() throws Exception {
		// Given
		int memoryLimit = compilerService.getMaxExecutionMemory() + 1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, 0, memoryLimit, Language.JAVA);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	/**
	 * When memory limit less than 0 should return bad request.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenMemoryLimitLessThanMinExecutionMemoryShouldReturnBadRequest() throws Exception {
		// Given
		int memoryLimit = compilerService.getMinExecutionMemory() - 1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, 0, memoryLimit, Language.JAVA);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	/**
	 * When image build failed should throw docker build exception.
	 */
	@Test
	void WhenImageBuildFailedShouldThrowDockerBuildException() {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
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
			compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		});
	}
	
	/**
	 * When image build succeed should return a result.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenImageBuildSucceedShouldReturnAResult() throws Exception {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(Verdict.ACCEPTED.getValue(), "", "");
		
		Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		
		// Then
		Assertions.assertEquals(ResponseEntity
				.status(HttpStatus.OK)
				.body(new Response(result.getOutput(), result.getExpectedOutput(), result.getVerdict(), null)).getStatusCode(), responseEntity.getStatusCode());
	}
	
	/**
	 * When its a correct answer compile method should return accepted verdict.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenItsACorrectAnswerCompileMethodShouldReturnAcceptedVerdict() throws Exception {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(Verdict.ACCEPTED.getValue(), "", "");
		
		Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(Verdict.ACCEPTED.getValue(), response.getStatus());
	}
	
	/**
	 * When its a wrong answer compile method should return wrong answer verdict.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenItsAWrongAnswerCompileMethodShouldReturnWrongAnswerVerdict() throws Exception {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(Verdict.WRONG_ANSWER.getValue(), "", "");
		
		Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(Verdict.WRONG_ANSWER.getValue(), response.getStatus());
	}
	
	/**
	 * When the execution time exceed the limit compile method should return time limit exceeded verdict.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenTheExecutionTimeExceedTheLimitCompileMethodShouldReturnTimeLimitExceededVerdict() throws Exception {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(Verdict.TIME_LIMIT_EXCEEDED.getValue(), "", "");
		
		Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(Verdict.TIME_LIMIT_EXCEEDED.getValue(), response.getStatus());
	}
	
	/**
	 * When there is a runtime error compile method should return run time error verdict.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenThereIsARuntimeErrorCompileMethodShouldReturnRunTimeErrorVerdict() throws Exception {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(Verdict.RUNTIME_ERROR.getValue(), "", "");
		
		Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(Verdict.RUNTIME_ERROR.getValue(), response.getStatus());
	}
	
	/**
	 * When memory limit exceeded compile method should return out of memory error verdict.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenMemoryLimitExceededCompileMethodShouldReturnOutOfMemoryErrorVerdict() throws Exception {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(Verdict.OUT_OF_MEMORY.getValue(), "", "");
		
		Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(Verdict.OUT_OF_MEMORY.getValue(), response.getStatus());
	}
	
	/**
	 * When it is a compilation error compile method should return compilation error verdict.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenItIsACompilationErrorCompileMethodShouldReturnCompilationErrorVerdict() throws Exception {
		// Given
		Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(0);
		
		Result result = new Result(Verdict.COMPILATION_ERROR.getValue(), "", "");
		
		Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(result);
		
		// MultipartFIle
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"hello.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes()
		);
		
		// When
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Language.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(Verdict.COMPILATION_ERROR.getValue(), response.getStatus());
	}
	
}
