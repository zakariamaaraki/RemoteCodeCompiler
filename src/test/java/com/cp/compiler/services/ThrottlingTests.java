package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import com.cp.compiler.services.businesslogic.CompilerProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ActiveProfiles("throttling")
@DirtiesContext
@SpringBootTest
class ThrottlingTests {
    
    @Autowired
    private CompilerProxy compilerProxy;
    
    private MultipartFile file = new MockMultipartFile(
            "test.java",
            "test.java",
            null,
            (byte[]) null);
    
    @Test
    void requestShouldBeThrottled() throws Exception {
        // Given
        var testCase = new ConvertedTestCase("id", file, file);
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
    
        // When
        ResponseEntity responseEntity = compilerProxy.execute(execution);
        
        // Then
        Assertions.assertEquals(HttpStatus.TOO_MANY_REQUESTS, responseEntity.getStatusCode());
    }
}
