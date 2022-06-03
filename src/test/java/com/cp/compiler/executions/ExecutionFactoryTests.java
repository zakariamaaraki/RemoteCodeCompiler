package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ExecutionFactoryTests {
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void shouldRegisterAProgrammingLanguage() {
        // Given
        JavaExecutionFactory javaExecutionFactory = new JavaExecutionFactory(null);
        
        // When
        ExecutionFactory.register(Language.JAVA, () -> javaExecutionFactory);
        Execution execution =
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.JAVA);
        // Then
        Assertions.assertNotNull(execution);
    }
    
    @Test
    void shouldThrowFactoryNotFoundException() {
        Assertions.assertThrows(
                FactoryNotFoundException.class,
                () -> ExecutionFactory.createExecution(file, file, file, 10, 100, Language.JAVA));
    }
}
