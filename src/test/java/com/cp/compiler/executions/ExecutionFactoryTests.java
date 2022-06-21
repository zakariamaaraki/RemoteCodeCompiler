package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.executions.c.CExecution;
import com.cp.compiler.executions.c.CExecutionFactory;
import com.cp.compiler.executions.cpp.CPPExecution;
import com.cp.compiler.executions.cpp.CPPExecutionFactory;
import com.cp.compiler.executions.cs.CSExecution;
import com.cp.compiler.executions.cs.CSExecutionFactory;
import com.cp.compiler.executions.go.GoExecution;
import com.cp.compiler.executions.go.GoExecutionFactory;
import com.cp.compiler.executions.java.JavaExecution;
import com.cp.compiler.executions.java.JavaExecutionFactory;
import com.cp.compiler.executions.kotlin.KotlinExecution;
import com.cp.compiler.executions.kotlin.KotlinExecutionFactory;
import com.cp.compiler.executions.python.PythonExecution;
import com.cp.compiler.executions.python.PythonExecutionFactory;
import com.cp.compiler.executions.rust.RustExecution;
import com.cp.compiler.executions.rust.RustExecutionFactory;
import com.cp.compiler.executions.scala.ScalaExecution;
import com.cp.compiler.executions.scala.ScalaExecutionFactory;
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
        JavaExecutionFactory javaExecutionFactory = new JavaExecutionFactory(null, null);
        
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
        var javaExecutionFactory = new JavaExecutionFactory(null, null);
        
        // When
        Execution execution = javaExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof JavaExecution);
    }
    
    @Test
    void shouldCreatePythonExecution() {
        // Given
        var pythonExecutionFactory = new PythonExecutionFactory(null, null);
        
        // When
        Execution execution = pythonExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof PythonExecution);
    }
    
    @Test
    void shouldCreateCExecution() {
        // Given
        var cExecutionFactory = new CExecutionFactory(null, null);
        
        // When
        Execution execution = cExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof CExecution);
    }
    
    @Test
    void shouldCreateCppExecution() {
        // Given
        var cppExecutionFactory = new CPPExecutionFactory(null, null);
        
        // When
        Execution execution = cppExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof CPPExecution);
    }
    
    @Test
    void shouldCreateGoExecution() {
        // Given
        var goExecutionFactory = new GoExecutionFactory(null, null);
        
        // When
        Execution execution = goExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof GoExecution);
    }
    
    
    @Test
    void shouldCreateCsExecution() {
        // Given
        var csExecutionFactory = new CSExecutionFactory(null, null);
        
        // When
        Execution execution = csExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof CSExecution);
    }
    
    @Test
    void shouldCreateKotlinExecution() {
        // Given
        var kotlinExecutionFactory = new KotlinExecutionFactory(null, null);
        
        // When
        Execution execution = kotlinExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof KotlinExecution);
    }
    
    @Test
    void shouldCreateScalaExecution() {
        // Given
        var scalaExecutionFactory = new ScalaExecutionFactory(null, null);
        
        // When
        Execution execution = scalaExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof ScalaExecution);
    }
    
    @Test
    void shouldCreateRustExecution() {
        // Given
        var rustExecutionFactory = new RustExecutionFactory(null, null);
        
        // When
        Execution execution = rustExecutionFactory.createExecution(file, file, file, 10, 100);
        
        // Then
        Assertions.assertNotNull(execution);
        Assertions.assertTrue(execution instanceof RustExecution);
    }
}
