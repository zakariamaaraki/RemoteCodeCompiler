package com.cp.compiler.e2e.problems;

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

// Problem from Codeforces
@Slf4j
@DirtiesContext
@SpringBootTest
class WatermelonTests {
    
    @Autowired
    private CompilerController compilerController;
    
    @DisplayName("Watermelon Problem test case 1")
    @Test
    void watermelonTest1ShouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/problems/Watermelon.java");
        MultipartFile sourceCode = new MockMultipartFile("Watermelon.java",
                                                         "Watermelon.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/watermelon/Watermelon-1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Watermelon-1.txt",
                                                             "Watermelon-1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        File inputFile = new File("src/test/resources/inputs/watermelon/Watermelon-1.txt");
        MultipartFile inputs = new MockMultipartFile("Watermelon-1.txt",
                                                     "Watermelon-1.txt",
                                                     null,
                                                     new FileInputStream(inputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                inputs,
                expectedOutput,
                3,
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
    
    @DisplayName("Watermelon Problem test case 2")
    @Test
    void watermelonTest2ShouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/problems/Watermelon.java");
        MultipartFile sourceCode = new MockMultipartFile("Watermelon.java",
                                                         "Watermelon.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/watermelon/Watermelon-2.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Watermelon-2.txt",
                                                             "Watermelon-2.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        File inputFile = new File("src/test/resources/inputs/watermelon/Watermelon-2.txt");
        MultipartFile inputs = new MockMultipartFile("Watermelon-2.txt",
                                                     "Watermelon-2.txt",
                                                     null,
                                                     new FileInputStream(inputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                inputs,
                expectedOutput,
                3,
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
    
    @DisplayName("Watermelon Problem test case 3")
    @Test
    void watermelonTest3ShouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/problems/Watermelon.java");
        MultipartFile sourceCode = new MockMultipartFile("Watermelon.java",
                                                         "Watermelon.java",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/watermelon/Watermelon-3.txt");
        MultipartFile expectedOutput = new MockMultipartFile("Watermelon-3.txt",
                                                             "Watermelon-3.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        File inputFile = new File("src/test/resources/inputs/watermelon/Watermelon-3.txt");
        MultipartFile inputs = new MockMultipartFile("Watermelon-3.txt",
                                                     "Watermelon-3.txt",
                                                     null,
                                                     new FileInputStream(inputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.JAVA,
                sourceCode,
                inputs,
                expectedOutput,
                3,
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
}
