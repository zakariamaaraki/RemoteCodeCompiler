package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerThrottlingException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.Language;
import com.cp.compiler.services.api.CompilerProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    void requestShouldBeThrottled() {
        // Given
        var testCase = new TransformedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
    
        // When / Then
        Assertions.assertThrows(CompilerThrottlingException.class, () -> {
            compilerProxy.execute(execution);
        });
    }
}
