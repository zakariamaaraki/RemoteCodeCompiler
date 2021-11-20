package com.cp.compiler;

import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.model.Languages;
import com.cp.compiler.model.Response;
import com.cp.compiler.model.Result;
import com.cp.compiler.service.CompilerService;
import com.cp.compiler.service.ContainerService;
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
	private static final String ACCEPTED_VERDICT = "Accepted";
	private static final String WRONG_ANSWER_VERDICT = "Wrong Answer";
	private static final String TIME_LIMIT_EXCEEDED_VERDICT = "Time Limit Exceeded";
	private static final String RUNTIME_ERROR_VERDICT = "Runtime Error";
	private static final String OUT_OF_MEMORY_ERROR_VERDICT = "Out Of Memory";
	private static final String COMPILATION_ERROR_VERDICT = "Compilation Error";
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
	void WhenTimeLimitGreaterThan15ShouldReturnBadRequest() throws Exception {
		// Given
		int timeLimit = 16;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, timeLimit, 500, Languages.JAVA);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	/**
	 * When time limit less than 0 should return bad request.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenTimeLimitLessThan0ShouldReturnBadRequest() throws Exception {
		// Given
		int timeLimit = -1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, timeLimit, 500, Languages.JAVA);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	/**
	 * When memory limit greater than 1000 should return bad request.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenMemoryLimitGreaterThan1000ShouldReturnBadRequest() throws Exception {
		// Given
		int memoryLimit = 1001;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, 0, memoryLimit, Languages.JAVA);
		
		// Then
		Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
	}
	
	/**
	 * When memory limit less than 0 should return bad request.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void WhenMemoryLimitLessThan0ShouldReturnBadRequest() throws Exception {
		// Given
		int memoryLimit = -1;
		
		// When
		ResponseEntity responseEntity = compilerService.compile(null, null, null, 0, memoryLimit, Languages.JAVA);
		
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
			compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
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
		
		Result result = new Result(ACCEPTED_VERDICT, "", "");
		
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
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
		
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
		
		Result result = new Result(ACCEPTED_VERDICT, "", "");
		
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
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(ACCEPTED_VERDICT, response.getStatus());
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
		
		Result result = new Result(WRONG_ANSWER_VERDICT, "", "");
		
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
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(WRONG_ANSWER_VERDICT, response.getStatus());
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
		
		Result result = new Result(TIME_LIMIT_EXCEEDED_VERDICT, "", "");
		
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
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(TIME_LIMIT_EXCEEDED_VERDICT, response.getStatus());
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
		
		Result result = new Result(RUNTIME_ERROR_VERDICT, "", "");
		
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
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(RUNTIME_ERROR_VERDICT, response.getStatus());
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
		
		Result result = new Result(OUT_OF_MEMORY_ERROR_VERDICT, "", "");
		
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
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(OUT_OF_MEMORY_ERROR_VERDICT, response.getStatus());
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
		
		Result result = new Result(COMPILATION_ERROR_VERDICT, "", "");
		
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
		ResponseEntity<Object> responseEntity = compilerService.compile(file, file, null, 10, 100, Languages.JAVA);
		
		// Then
		Response response = (Response) responseEntity.getBody();
		Assertions.assertEquals(COMPILATION_ERROR_VERDICT, response.getStatus());
	}
	
}
