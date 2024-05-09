package com.cp.compiler.security;

import com.cp.compiler.api.controllers.CompilerController;
import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.contract.testcases.TestCaseResult;
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
class PythonSecurityTests {
    
    @Autowired
    private CompilerController compilerController;
    
    /**
     * Running a system level command should return runtime error statusResponse.
     *
     * @throws Exception the exception
     */
    @DisplayName("Python Security Command Line execution Should Return an Error")
    @Test
    void RunningASystemCommandShouldReturnRuntimeErrorVerdict() throws Exception {
        // Given
        File sourceCodeFile = new File("src/test/resources/sources/security/py/CommandLine.py");
        MultipartFile sourceCode = new MockMultipartFile(
                "Test1.c",
                "Test1.c",
                null,
                new FileInputStream(sourceCodeFile));
        
        // Dummy expected output
        File expectedOutputFile = new File("src/test/resources/outputs/Test1.txt");
        MultipartFile expectedOutput = new MockMultipartFile(
                "Test1.txt",
                "Test1.txt",
                null,
                new FileInputStream(expectedOutputFile));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerController.compile(
                Language.C,
                sourceCode,
                null,
                expectedOutput,
                10,
                500,
                null,
                null,
                "");
    
        for (TestCaseResult testCaseResult: responseEntity.getBody().getExecution().getTestCasesResult().values()) {
            log.info("Container std output = {}", testCaseResult.getOutput());
            log.info("Container stderr output = {}", testCaseResult.getError());
        }
        
        // Then
        Assertions.assertFalse(
                responseEntity
                        .getBody()
                        .getExecution()
                        .getError()
                        .isEmpty());
    }
}
