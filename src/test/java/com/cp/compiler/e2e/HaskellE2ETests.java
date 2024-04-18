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

@Slf4j
@DirtiesContext
@SpringBootTest
class HaskellE2ETests {
    
    @Autowired
    private CompilerController compilerController;
    
    /**
     * Should return accepted statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Haskell Accepted Verdict")
    @Test
    void shouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/haskell/Test1.hs");
        MultipartFile sourceCode = new MockMultipartFile("Test1.hs",
                "Test1.hs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.HASKELL,
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
    @DisplayName("Haskell Time Limit Exceeded")
    @Test
    void shouldReturnTimeLimitExceededVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/haskell/Test2.hs");
        MultipartFile sourceCode = new MockMultipartFile("Test2.hs",
                "Test2.hs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.HASKELL,
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
     * Should return compilation error statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Haskell Compilation Error")
    @Test
    void shouldReturnCompilationErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/haskell/Test3.hs");
        MultipartFile sourceCode = new MockMultipartFile("Test3.hs",
                "Test3.hs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.HASKELL,
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
    @DisplayName("Haskell Wrong Answer")
    @Test
    void shouldReturnWrongAnswerVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/haskell/Test4.hs");
        MultipartFile sourceCode = new MockMultipartFile("Test4.hs",
                "Test4.hs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.HASKELL,
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
    @DisplayName("Haskell Out Of Memory Error")
    @Test
    void shouldReturnOutOfMemoryVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/haskell/Test5.hs");
        MultipartFile sourceCode = new MockMultipartFile("Test5.hs",
                "Test5.hs",
                null,
                new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.HASKELL,
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
}
