package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.executions.languages.JavaExecution;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@DirtiesContext
@SpringBootTest
public class ExecutionFactoryTests {
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    // This test will affect all other tests, make sure the context is deleted.
    @Test
    void shouldRegisterAProgrammingLanguage() {
        
        // Given
        ExecutionFactory.register(
                Language.JAVA,
                (MultipartFile sourceCode, List<ConvertedTestCase> testCases, int timeLimit, int memoryLimit) -> {
                    return new JavaExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            null,
                            null);
        });
        
        // When
        var testCase = new ConvertedTestCase("id", file, file);
        Execution execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof JavaExecution);
    }
    
    @Test
    void shouldThrowFactoryNotFoundException() {
        var testCase = new ConvertedTestCase("id", file, file);
        
        Assertions.assertThrows(
                FactoryNotFoundException.class,
                () -> ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, null));
    }
}
