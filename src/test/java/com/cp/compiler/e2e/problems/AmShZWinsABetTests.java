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

@Slf4j
@DirtiesContext
@SpringBootTest
class AmShZWinsABetTests {
    
    @Autowired
    private CompilerController compilerController;
    
    @DisplayName("AmShZ Wins a Bet Problem test case 1")
    @Test
    void amShZWinsABetTest1ShouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/problems/AmShZWinsABet.c");
        MultipartFile sourceCode = new MockMultipartFile("AmShZWinsABet.c",
                                                         "AmShZWinsABet.c",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/amShZWinsABet/amShZWinsABet-1.txt");
        MultipartFile expectedOutput = new MockMultipartFile("amShZWinsABet-1.txt",
                                                             "amShZWinsABet-1.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        File inputFile = new File("src/test/resources/inputs/amShZWinsABet/amShZWinsABet-1.txt");
        MultipartFile inputs = new MockMultipartFile("amShZWinsABet-1.txt",
                                                     "amShZWinsABet-1.txt",
                                                     null,
                                                     new FileInputStream(inputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.C,
                sourceCode,
                inputs,
                expectedOutput,
                3,
                2000,
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
    
    @DisplayName("AmShZ Wins a Bet Problem test case 2")
    @Test
    void amShZWinsABetTest2ShouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/problems/AmShZWinsABet.c");
        MultipartFile sourceCode = new MockMultipartFile("AmShZWinsABet.c",
                                                         "AmShZWinsABet.c",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/amShZWinsABet/amShZWinsABet-2.txt");
        MultipartFile expectedOutput = new MockMultipartFile("amShZWinsABet-2.txt",
                                                             "amShZWinsABet-2.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        File inputFile = new File("src/test/resources/inputs/amShZWinsABet/amShZWinsABet-2.txt");
        MultipartFile inputs = new MockMultipartFile("amShZWinsABet-2.txt",
                                                     "amShZWinsABet-2.txt",
                                                     null,
                                                     new FileInputStream(inputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.C,
                sourceCode,
                inputs,
                expectedOutput,
                3,
                2000,
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
    
    @DisplayName("AmShZ Wins a Bet Problem test case 3")
    @Test
    void amShZWinsABetTestsTest3ShouldReturnAcceptedVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/problems/AmShZWinsABet.c");
        MultipartFile sourceCode = new MockMultipartFile("AmShZWinsABet.c",
                                                         "AmShZWinsABet.c",
                                                         null,
                                                         new FileInputStream(sourceCodeFile));
        
        File expectedOutputFile = new File("src/test/resources/outputs/amShZWinsABet/amShZWinsABet-3.txt");
        MultipartFile expectedOutput = new MockMultipartFile("amShZWinsABet-3.txt",
                                                             "amShZWinsABet-3.txt",
                                                             null,
                                                             new FileInputStream(expectedOutputFile));
        
        File inputFile = new File("src/test/resources/inputs/amShZWinsABet/amShZWinsABet-3.txt");
        MultipartFile inputs = new MockMultipartFile("amShZWinsABet-3.txt",
                                                     "amShZWinsABet-3.txt",
                                                     null, new FileInputStream(inputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.C,
                sourceCode,
                inputs,
                expectedOutput,
                3,
                2000,
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
