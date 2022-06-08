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

/**
 * Kotlin e2e tests.
 */
@DirtiesContext
@SpringBootTest
class KotlinE2ETests {
    
    @Autowired
    private CompilerController compilerController;
    
    /**
     * Should return accepted verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("Kotlin Accepted Verdict")
    @Test
    void shouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/kotlin/Test1.kt");
        MultipartFile sourceCode = new MockMultipartFile("Test1.kt",
                                                         "Test1.kt",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity responseEntity = compilerController.compile(
                Language.KOTLIN,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
    
        // Then
        Assertions.assertEquals(Verdict.ACCEPTED.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return time limit exceeded verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("Kotlin Time Limit Exceeded")
    @Test
    void shouldReturnTimeLimitExceededVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/kotlin/Test2.kt");
        MultipartFile sourceCode = new MockMultipartFile("Test2.kt",
                                                         "Test2.kt",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity responseEntity = compilerController.compile(
                Language.KOTLIN,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(Verdict.TIME_LIMIT_EXCEEDED.getStatusResponse(),
                                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return compilation error verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("Kotlin Compilation Error")
    @Test
    void shouldReturnCompilationErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/kotlin/Test3.kt");
        MultipartFile sourceCode = new MockMultipartFile("Test3.kt",
                                                         "Test3.kt",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity responseEntity = compilerController.compile(
                Language.KOTLIN,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(Verdict.COMPILATION_ERROR.getStatusResponse()
                , ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return wrong answer verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("Kotlin Wrong Answer")
    @Test
    void shouldReturnWrongAnswerVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/kotlin/Test4.kt");
        MultipartFile sourceCode = new MockMultipartFile("Test4.kt",
                                                         "Test4.kt",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity responseEntity = compilerController.compile(
                Language.KOTLIN,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(Verdict.WRONG_ANSWER.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return out of memory verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("Kotlin Out Of Memory Error")
    @Test
    void shouldReturnOutOfMemoryVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/kotlin/Test5.kt");
        MultipartFile sourceCode = new MockMultipartFile("Test5.kt",
                                                         "Test5.kt",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity responseEntity = compilerController.compile(
                Language.KOTLIN,
                expectedOutput,
                sourceCode,
                null,
                10,
                1,
                null,
                null);
        
        // Then
        Assertions.assertEquals(Verdict.OUT_OF_MEMORY.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    
    /**
     * Should return runtime error verdict.
     *
     * @throws Exception the exception
     */
    @DisplayName("Kotlin Runtime Error")
    @Test
    void shouldReturnRuntimeErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/kotlin/Test6.kt");
        MultipartFile sourceCode = new MockMultipartFile("Test6.kt",
                                                         "Test6.kt",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Test1.txt",
                                                             "Test1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity responseEntity = compilerController.compile(
                Language.KOTLIN,
                expectedOutput,
                sourceCode,
                null,
                10,
                500,
                null,
                null);
        
        // Then
        Assertions.assertEquals(Verdict.RUNTIME_ERROR.getStatusResponse(),
                ((Response)responseEntity.getBody()).getResult().getStatusResponse());
    }
    

}
