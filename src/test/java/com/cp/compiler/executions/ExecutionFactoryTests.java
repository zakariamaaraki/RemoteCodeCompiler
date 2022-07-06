package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

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
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new JavaExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            null);
        });
        
        // When
        Execution execution =
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.JAVA);
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof JavaExecution);
    }
    
    @Test
    void shouldThrowFactoryNotFoundException() {
        Assertions.assertThrows(
                FactoryNotFoundException.class,
                () -> ExecutionFactory.createExecution(file, file, file, 10, 100, null));
    }
}
