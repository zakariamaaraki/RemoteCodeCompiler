package com.cp.compiler.e2e;

import com.cp.compiler.controllers.CompilerController;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Verdict;
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

@DirtiesContext
@SpringBootTest
public class CSE2ETests {
    
    @Autowired
    private CompilerController compilerController;
    
    /**
     * Should return accepted verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("CS Accepted Verdict")
    @Test
    void shouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/cs/Test1.cs");
        MultipartFile sourceCode = new MockMultipartFile(
                "Test1.cs",
                "Test1.cs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile(
                "Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<Object> responseEntity = compilerController.compile(
                Language.CS,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(
                Verdict.ACCEPTED.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return time limit exceeded verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("CS Time Limit Exceeded")
    @Test
    void shouldReturnTimeLimitExceededVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/cs/Test2.cs");
        MultipartFile sourceCode = new MockMultipartFile(
                "Test2.cs",
                "Test2.cs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile(
                "Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<Object> responseEntity = compilerController.compile(
                Language.CS,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(
                Verdict.TIME_LIMIT_EXCEEDED.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return compilation error verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("CS Compilation Error")
    @Test
    void shouldReturnCompilationErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/cs/Test3.cs");
        MultipartFile sourceCode = new MockMultipartFile(
                "Test3.cs",
                "Test3.cs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile(
                "Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<Object> responseEntity = compilerController.compile(
                Language.CS,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(
                Verdict.COMPILATION_ERROR.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return wrong answer verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("CS Wrong Answer")
    @Test
    void shouldReturnWrongAnswerVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/cs/Test4.cs");
        MultipartFile sourceCode = new MockMultipartFile(
                "Test4.cs",
                "Test4.cs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile(
                "Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<Object> responseEntity = compilerController.compile(
                Language.CS,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(
                Verdict.WRONG_ANSWER.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return out of memory verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("CS Out Of Memory Error")
    @Test
    void shouldReturnOutOfMemoryVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/cs/Test5.cs");
        MultipartFile sourceCode = new MockMultipartFile(
                "Test5.cs",
                "Test5.cs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile(
                "Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<Object> responseEntity = compilerController.compile(
                Language.CS,
                expectedOutput,
                sourceCode,
                null,
                10,
                1,
                null,
                null);
        
        // Then
        Assertions.assertEquals(
                Verdict.OUT_OF_MEMORY.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return runtime error verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("CS Runtime Error")
    @Test
    void shouldReturnRuntimeErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/cs/Test6.cs");
        MultipartFile sourceCode = new MockMultipartFile("Test6.cs",
                "Test6.cs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<Object> responseEntity = compilerController.compile(
                Language.CS,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(
                Verdict.RUNTIME_ERROR.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
}
