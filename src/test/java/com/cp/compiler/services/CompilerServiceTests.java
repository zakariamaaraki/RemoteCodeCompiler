package com.cp.compiler.services;

import com.cp.compiler.exceptions.ContainerBuildException;
import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.*;
import com.cp.compiler.services.containers.ContainerService;
import com.cp.compiler.utils.StatusUtils;
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
    void WhenTimeLimitGreaterThanMaxExecutionTimeShouldReturnBadRequest() {
        // Given
        int timeLimit = Integer.MAX_VALUE;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, timeLimit, 100, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.execute(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    /**
     * When time limit less than 0 should return bad request.
     *
     * @throws Exception the exception
     */
    @Test
    void WhenTimeLimitLessThanMinExecutionTimeShouldReturnBadRequest() {
        // Given
        int timeLimit = -1;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, timeLimit, 100, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.execute(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    /**
     * When memory limit greater than 1000 should return bad request.
     *
     * @throws Exception the exception
     */
    @Test
    void WhenMemoryLimitGreaterThanMaxExecutionMemoryShouldReturnBadRequest() {
        // Given
        int memoryLimit = Integer.MAX_VALUE;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, memoryLimit, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.execute(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    /**
     * When memory limit less than 0 should return bad request.
     *
     * @throws Exception the exception
     */
    @Test
    void WhenMemoryLimitLessThanMinExecutionMemoryShouldReturnBadRequest() {
        // Given
        int memoryLimit = -1;
    
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, memoryLimit, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.execute(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    /**
     * When image build failed should throw container build exception.
     */
    @Test
    void WhenImageBuildFailShouldThrowContainerBuildException() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerBuildException("Error Building image"));
    
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS).build());
        
        // When / Then
        Assertions.assertThrows(ContainerBuildException.class, () -> {
            compilerService.execute(execution);
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
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        String output = "test";
        String expectedOutput = "test";
        
        Result result = new Result(Verdict.ACCEPTED, output, "", expectedOutput, 0);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .stdErr("")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
    
        // Execution Container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(containerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(containerOutput);
        
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
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
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
    
        String output = "test2";
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .stdErr("")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
    
        // Execution Container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(containerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(containerOutput);
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
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
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
    
        String output = "test";
        String expectedOutput = "not a test";
    
        MockMultipartFile sourceCode = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        MockMultipartFile expectedOutputFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                expectedOutput.getBytes()
        );
    
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .stdErr("")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
    
        // Execution Container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(containerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(containerOutput);
    
        Execution execution = ExecutionFactory.createExecution(
                sourceCode, null, expectedOutputFile, 10, 100, Language.JAVA);
    
        // When
        ResponseEntity responseEntity = compilerService.execute(execution);
        
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
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
    
        String output = "test";
        String expectedOutput = "not a test";
    
        Result result = new Result(Verdict.ACCEPTED, output, "", expectedOutput, 0);
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        ProcessOutput executionContainerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .stdErr("")
                .status(StatusUtils.TIME_LIMIT_EXCEEDED_STATUS)
                .build();
    
        ProcessOutput compilationContainerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
    
        // Execution Container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(executionContainerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(compilationContainerOutput);
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
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
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
    
        String output = "test";
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        ProcessOutput executionContainerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .stdErr("")
                .status(999) // Runtime error
                .build();
    
        ProcessOutput compilationContainerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .stdErr("")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
    
        // Execution Container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(executionContainerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(compilationContainerOutput);
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
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
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
    
        String output = "test";
        String expectedOutput = "not a test";
    
        Result result = new Result(Verdict.ACCEPTED, output, "", expectedOutput, 0);
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        ProcessOutput executionContainerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .stdErr("")
                .status(StatusUtils.OUT_OF_MEMORY_STATUS)
                .build();
    
        ProcessOutput compilationContainerOutput = ProcessOutput
                .builder()
                .stdOut(output)
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
    
        // Execution Container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(executionContainerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(compilationContainerOutput);
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
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
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
    
        String output = "test";
        String error = "compilation error";
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdErr(error)
                .status(StatusUtils.COMPILATION_ERROR_STATUS)
                .build();
    
        // Execution container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(containerOutput);
    
        // Compilation container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.COMPILATION_ERROR.getStatusResponse(), response.getResult().getStatusResponse());
    }
    
    @Test
    void shouldThrownContainerFailedDependencyException() throws Exception {
        // Given
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        String output = "test";
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
        
        // Execution container
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenThrow(new ContainerFailedDependencyException("Docker engine error"));
    
        // Compilation container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenThrow(new ContainerFailedDependencyException("Docker engine error"));
        
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        // Then
        Assertions.assertThrows(ContainerFailedDependencyException.class, () -> compilerService.execute(execution));
    }
    
    @Test
    void defaultCompilerShouldThrowContainerOperationTimeoutException() throws Exception {
        // Given
        String output = "test";
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerOperationTimeoutException("exception"));
        
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS).build());
        
        // When / Then
        Assertions.assertThrows(ContainerBuildException.class, () -> compilerService.execute(execution));
    }
    
    @Test
    void defaultCompilerShouldCleanStderrOutput() {
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "output".getBytes()
        );
        
        Execution execution = ExecutionFactory.createExecution(
                file, null, file, 10, 100, Language.JAVA);
        
        var processOutput =
                ProcessOutput
                        .builder()
                        .stdOut("")
                        .stdErr(execution.getPath() + "/mockFile")
                        .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                        .build();
        
        Mockito.when(containerService.runContainer(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(processOutput);
        
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS).build());
        
        // When
        ResponseEntity<Object> response = compilerService.execute(execution);
        
        // Then
        Assertions.assertEquals("/mockFile", ((Response)response.getBody()).getResult().getError());
    }
}
