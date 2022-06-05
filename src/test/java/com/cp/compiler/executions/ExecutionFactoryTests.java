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
                () -> ExecutionFactory.createExecution(file, file, file, 10, 100, null));
    }
    
    @Test
    void shouldCreateJavaExecution() {
        // Given
        var javaExecutionFactory = new JavaExecutionFactory(null);
        
        // When
        Execution execution =
                javaExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof JavaExecution);
    }
    
    @Test
    void shouldCreatePythonExecution() {
        // Given
        var pythonExecutionFactory = new PythonExecutionFactory(null);
        
        // When
        Execution execution =
                pythonExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof PythonExecution);
    }
    
    @Test
    void shouldCreateCExecution() {
        // Given
        var cExecutionFactory = new CExecutionFactory(null);
        
        // When
        Execution execution =
                cExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof CExecution);
    }
    
    @Test
    void shouldCreateCppExecution() {
        // Given
        var cppExecutionFactory = new CPPExecutionFactory(null);
        
        // When
        Execution execution =
                cppExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof CPPExecution);
    }
    
    @Test
    void shouldCreateGoExecution() {
        // Given
        var goExecutionFactory = new GoExecutionFactory(null);
        
        // When
        Execution execution =
                goExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof GoExecution);
    }
}
