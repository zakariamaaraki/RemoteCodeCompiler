package com.cp.compiler.e2e;

import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.api.controllers.CompilerController;
import com.cp.compiler.contract.Language;
import com.cp.compiler.models.Verdict;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

/**
 * Java e2e tests.
 */
@Slf4j
@DirtiesContext
@SpringBootTest
class JavaE2ETests {
    
    @Autowired
    private CompilerController compilerController;
    
    /**
     * Should return accepted statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Accepted Verdict")
    @Test
    void shouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/Test1.java");
        MultipartFile sourceCode = new MockMultipartFile("Test1.java",
                                                         "Test1.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.ACCEPTED.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    /**
     * Should return time limit exceeded statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Time Limit Exceeded")
    @Test
    void shouldReturnTimeLimitExceededVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/Test2.java");
        MultipartFile sourceCode = new MockMultipartFile("Test2.java",
                                                         "Test2.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.TIME_LIMIT_EXCEEDED.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    /**
     * Should return time limit exceeded statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Time Limit Exceeded 2")
    @Test
    void shouldAlsoReturnTimeLimitExceededVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/Test7.java");
        MultipartFile sourceCode = new MockMultipartFile("Test7.java",
                                                         "Test7.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                5,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.TIME_LIMIT_EXCEEDED.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    /**
     * Should return compilation error statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Compilation Error")
    @Test
    void shouldReturnCompilationErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/Test3.java");
        MultipartFile sourceCode = new MockMultipartFile("Test3.java",
                                                         "Test3.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.COMPILATION_ERROR.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    /**
     * Should return wrong answer statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Wrong Answer")
    @Test
    void shouldReturnWrongAnswerVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/Test4.java");
        MultipartFile sourceCode = new MockMultipartFile("Test4.java",
                                                         "Test4.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.WRONG_ANSWER.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    /**
     * Should return out of memory statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Out Of Memory Error")
    @Test
    void shouldReturnOutOfMemoryVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/Test5.java");
        MultipartFile sourceCode = new MockMultipartFile("Test5.java",
                                                         "Test5.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                1,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.OUT_OF_MEMORY.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    /**
     * Should return runtime error statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Runtime Error")
    @Test
    void shouldReturnRuntimeErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/Test6.java");
        MultipartFile sourceCode = new MockMultipartFile("Test6.java",
                                                         "Test6.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.RUNTIME_ERROR.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    
    /**
     * Should return runtime error statusResponse for risky sourcecode.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Runtime Error for risky files")
    @Test
    void shouldReturnRuntimeErrorVerdictForRiskyFiles() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/CreateFiles.java");
        MultipartFile sourceCode = new MockMultipartFile("CreateFiles.java",
                "CreateFiles.java",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.RUNTIME_ERROR.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
    
    
    /**
     * Should return runtime error statusResponse for process execution.
     *
     * @throws Exception the exception
     */
    @DisplayName("Java Runtime Error for process execution")
    @Test
    void shouldReturnRuntimeErrorVerdictForProcessExecution() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/java/ProcessExecution.java");
        MultipartFile sourceCode = new MockMultipartFile("ProcessExecution.java",
                "ProcessExecution.java",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
        
        // Then
        Assertions.assertEquals(
                Verdict.RUNTIME_ERROR.getStatusResponse(),
                responseEntity
                        .getBody()
                        .getExecution()
                        .getVerdict());
    }
}
