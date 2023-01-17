package com.cp.compiler.services;

import com.cp.compiler.exceptions.*;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.*;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.testcases.TestCaseResult;
import com.cp.compiler.services.businesslogic.CompilerService;
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

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

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
            "test.java",
            "test.java",
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
    
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), timeLimit, 100, Language.JAVA);
        
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
    
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), timeLimit, 100, Language.JAVA);
        
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
    
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, memoryLimit, Language.JAVA);
        
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
    
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, memoryLimit, Language.JAVA);
        
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
                "file.java",
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        var testCase = new ConvertedTestCase("id", null, file);
    
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerBuildException("Error Building image"));
    
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
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
        
        TestCaseResult result = new TestCaseResult(Verdict.ACCEPTED, output, "", expectedOutput, 0);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
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
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
    
        var testCase = new ConvertedTestCase("id", null, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
        
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);

        // Then
        LinkedHashMap<String, TestCaseResult> testCasesResult = new LinkedHashMap<>();
        testCasesResult.put("id", result);
        
        var response = new Response(
                result.getVerdict().getStatusResponse(),
                result.getVerdict().getStatusCode(),
                "",
                testCasesResult,
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        Assertions.assertEquals(ResponseEntity
                                    .status(HttpStatus.OK)
                                    .body(response)
                                    .getStatusCode(),
                                responseEntity.getStatusCode());
    }
    
    /**
     * When its a correct answer compile method should return accepted statusResponse.
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
                "hello.java",
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
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
    
        var testCase = new ConvertedTestCase("id", null, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.ACCEPTED.getStatusResponse(), response.getVerdict());
    }
    
    /**
     * When its a wrong answer compile method should return wrong answer statusResponse.
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
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        MockMultipartFile expectedOutputFile = new MockMultipartFile(
                "file",
                "hello.java",
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
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
    
        var testCase = new ConvertedTestCase("id", null, expectedOutputFile);
        
        Execution execution =
                ExecutionFactory.createExecution(sourceCode, List.of(testCase), 10, 100, Language.JAVA);
    
        // When
        ResponseEntity responseEntity = compilerService.execute(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.WRONG_ANSWER.getStatusResponse(), response.getVerdict());
    }
    
    /**
     * When the execution time exceed the limit compile method should return time limit exceeded statusResponse.
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
    
        TestCaseResult result = new TestCaseResult(Verdict.ACCEPTED, output, "", expectedOutput, 0);
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
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
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(executionContainerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(compilationContainerOutput);
    
        var testCase = new ConvertedTestCase("id", null, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.TIME_LIMIT_EXCEEDED.getStatusResponse(), response.getVerdict());
    }
    
    /**
     * When there is a runtime error compile method should return run time error statusResponse.
     *
     * @throws Exception the exception
     */
    @Test
    void WhenThereIsARuntimeErrorCompileMethodShouldReturnRunTimeErrorVerdict() throws Exception {
        // Given
        Mockito.when(containerService.buildImage(
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn("build log");
    
        String output = "test";
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
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
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(executionContainerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(compilationContainerOutput);
    
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.RUNTIME_ERROR.getStatusResponse(), response.getVerdict());
    }
    
    /**
     * When memory limit exceeded compile method should return out of memory error statusResponse.
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
    
        TestCaseResult result = new TestCaseResult(Verdict.ACCEPTED, output, "", expectedOutput, 0);
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
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
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(executionContainerOutput);
    
        // Compilation Container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(compilationContainerOutput);
    
        var testCase = new ConvertedTestCase("id", null, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.OUT_OF_MEMORY.getStatusResponse(), response.getVerdict());
    }
    
    /**
     * When it is a compilation error compile method should return compilation error statusResponse.
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
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdErr(error)
                .status(StatusUtils.COMPILATION_ERROR_STATUS)
                .build();
    
        // Execution container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
    
        // Compilation container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
    
        var testCase = new ConvertedTestCase("id", null, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        // When
        ResponseEntity<Object> responseEntity = compilerService.execute(execution);
        
        // Then
        Response response = (Response) responseEntity.getBody();
        Assertions.assertEquals(Verdict.COMPILATION_ERROR.getStatusResponse(), response.getVerdict());
    }
    
    @Test
    void shouldThrowContainerFailedDependencyException() {
        // Given
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        String output = "test";
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
        
        // Execution container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenThrow(new ContainerFailedDependencyException("Docker engine error"));
    
        // Compilation container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenThrow(new ContainerFailedDependencyException("Docker engine error"));
    
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
        
        // Then
        Assertions.assertThrows(ContainerFailedDependencyException.class, () -> compilerService.execute(execution));
    }
    
    @Test
    void shouldThrowCompilationTimeoutException() {
        // Given
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        String output = "test";
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
        
        // Compilation container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().stdErr("").status(StatusUtils.TIME_LIMIT_EXCEEDED_STATUS).build());
        
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
        
        // Then
        Assertions.assertThrows(CompilationTimeoutException.class, () -> compilerService.execute(execution));
    }
    
    @Test
    void shouldThrowResourceLimitReachedException() {
        // Given
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        String output = "test";
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
        
        // Compilation container
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().stdErr("").status(StatusUtils.OUT_OF_MEMORY_STATUS).build());
        
        var testCase = new ConvertedTestCase("id", file, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
        
        // Then
        Assertions.assertThrows(ResourceLimitReachedException.class, () -> compilerService.execute(execution));
    }
    
    @Test
    void defaultCompilerShouldThrowContainerOperationTimeoutException() {
        // Given
        String output = "test";
    
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                output.getBytes()
        );
    
        var testCase = new ConvertedTestCase("id", null, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerOperationTimeoutException("exception"));
        
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS).build());
        
        // When / Then
        Assertions.assertThrows(ContainerOperationTimeoutException.class, () -> compilerService.execute(execution));
    }
    
    @Test
    void defaultCompilerShouldCleanStderrOutput() {
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.java",
                MediaType.TEXT_PLAIN_VALUE,
                "output".getBytes()
        );
    
        var testCase = new ConvertedTestCase("id", null, file);
        
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
        
        var processOutput =
                ProcessOutput
                        .builder()
                        .stdOut("")
                        .stdErr(execution.getPath() + "/mockFile")
                        .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                        .build();
        
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(processOutput);
        
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS).build());
        
        // When
        ResponseEntity<Object> response = compilerService.execute(execution);
        
        // Then
        Assertions.assertEquals(
                "/mockFile",
                ((Response)response.getBody()).getTestCasesResult().get("id").getError());
    
        Assertions.assertEquals(
                "/mockFile",
                ((Response)response.getBody()).getError());
    }
}
