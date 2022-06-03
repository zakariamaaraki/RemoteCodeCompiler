package com.cp.compiler.services;

import com.cp.compiler.exceptions.ContainerBuildException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Result;
import com.cp.compiler.models.Verdict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Compiler service tests.
 */
@DirtiesContext
@SpringBootTest
class CompilerServiceTests {
    
    private static final int BAD_REQUEST = 400;
    
    @MockBean
    private ContainerService containerService;
    
    @Qualifier("proxy")
    @Autowired
    private CompilerService compilerService;

    private MultipartFile file = new MockMultipartFile(
            "test.txt.c",
            "test.txt",
            null,
            (byte[]) null);
    
    /**
     * When time limit greater than 15 should return bad request.
     *
     * @throws Exception the exception
     */
    @Test
    void WhenTimeLimitGreaterThanMaxExecutionTimeShouldReturnBadRequest() throws Exception {
        // Given
        int timeLimit = Integer.MAX_VALUE;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, timeLimit, 100, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.compile(execution);
        
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
        int timeLimit = -1;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, timeLimit, 100, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.compile(execution);
        
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
        int memoryLimit = Integer.MAX_VALUE;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, memoryLimit, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.compile(execution);
        
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
        int memoryLimit = -1;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, memoryLimit, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.compile(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    /**
     * When image build failed should throw container build exception.
     */
    @Test
    void WhenImageBuildFailShouldThrowContainerBuildException() {
        // Given
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerBuildException("Error Building image"));
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // Then
        Assertions.assertThrows(ContainerBuildException.class, () -> {
            // When
            compilerService.compile(execution);
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
        
        Result result = new Result(Verdict.ACCEPTED, "file.txt", "file.txt", 0);
        
        Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.compile(execution);
        
        // Then
        Assertions.assertEquals(ResponseEntity
                                    .status(HttpStatus.OK)
                                    .body(new Response(result, null))
                                    .getStatusCode(),
                                responseEntity.getStatusCode());
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
        
        Result result = new Result(Verdict.ACCEPTED, "file.txt", "file.txt", 0);
        
        Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.compile(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.ACCEPTED.getStatusResponse(), response.getResult().getStatusResponse());
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
        
        Result result = new Result(Verdict.WRONG_ANSWER, "file.txt", "file.txt", 0);
        
        Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.compile(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.WRONG_ANSWER.getStatusResponse(), response.getResult().getStatusResponse());
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
        
        Result result = new Result(
                Verdict.TIME_LIMIT_EXCEEDED, "file.txt", "file.txt", 0);
        
        Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.compile(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.TIME_LIMIT_EXCEEDED.getStatusResponse(), response.getResult().getStatusResponse());
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
        
        Result result = new Result(
                Verdict.RUNTIME_ERROR, "file.txt", "file.txt", 0);
        
        Mockito.when(
                containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.compile(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.RUNTIME_ERROR.getStatusResponse(), response.getResult().getStatusResponse());
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
        
        Result result = new Result(
                Verdict.OUT_OF_MEMORY, "file.txt", "file.txt", 0);
        
        Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.compile(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.OUT_OF_MEMORY.getStatusResponse(), response.getResult().getStatusResponse());
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
        
        Result result = new Result(
                Verdict.COMPILATION_ERROR, "file.txt", "file.txt", 0);
        
        Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.compile(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.COMPILATION_ERROR.getStatusResponse(), response.getResult().getStatusResponse());
    }
    
}
