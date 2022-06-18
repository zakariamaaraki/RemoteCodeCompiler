package com.cp.compiler.executions;

import com.cp.compiler.executions.c.CExecutionFactory;
import com.cp.compiler.executions.cpp.CPPExecutionFactory;
import com.cp.compiler.executions.cs.CSExecutionFactory;
import com.cp.compiler.executions.go.GoExecutionFactory;
import com.cp.compiler.executions.java.JavaExecutionFactory;
import com.cp.compiler.executions.kotlin.KotlinExecutionFactory;
import com.cp.compiler.executions.python.PythonExecutionFactory;
import com.cp.compiler.models.Language;
import com.cp.compiler.templates.EntrypointFileGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@DirtiesContext
@SpringBootTest
public class ExecutionTests {
    
    @Autowired
    private EntrypointFileGenerator entrypointFileGenerator;
    
    private static final String DOCKERFILE = "Dockerfile";
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void shouldCreateAnExecutionEnvironment() throws IOException {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 500, Language.JAVA);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        File executionFolder = new File(execution.getPath());
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isDirectory());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void shouldDeleteTheExecutionEnvironment() throws IOException {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 500, Language.JAVA);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        File executionFolder = new File(execution.getPath());
        
        execution.deleteExecutionDirectory();
        Assertions.assertFalse(executionFolder.exists());
    }
    
    @Test
    void javaExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var javaExecutionFactory = new JavaExecutionFactory(null, entrypointFileGenerator);
        Execution execution = javaExecutionFactory.createExecution(
                file, file, file, 10, 500);
    
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
    
        // Then
        File executionFolder =
                new File(execution.getPath() + "/entrypoint.sh");
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
    
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void pythonExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var pythonExecutionFactory = new PythonExecutionFactory(null, entrypointFileGenerator);
        Execution execution = pythonExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder =
                new File(execution.getPath() + "/entrypoint.sh");
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void cExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var cExecutionFactory = new CExecutionFactory(null, entrypointFileGenerator);
        Execution execution = cExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder =
                new File(execution.getPath() + "/entrypoint.sh");
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void cppExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var cppExecutionFactory = new CPPExecutionFactory(null, entrypointFileGenerator);
        Execution execution = cppExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder =
                new File(execution.getPath() + "/entrypoint.sh");
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void csExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var csExecutionFactory = new CSExecutionFactory(null, entrypointFileGenerator);
        Execution execution = csExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder =
                new File(execution.getPath() + "/entrypoint.sh");
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void kotlinExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var kotlinExecutionFactory = new KotlinExecutionFactory(null, entrypointFileGenerator);
        Execution execution = kotlinExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder =
                new File(execution.getPath() + "/entrypoint.sh");
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void goExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var goExecutionFactory = new GoExecutionFactory(null, entrypointFileGenerator);
        Execution execution = goExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder =
                new File(execution.getPath() + "/entrypoint.sh");
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void shouldCopyDockerFileToExecutionDirectory() throws IOException {
        // Given
        var goExecutionFactory = new GoExecutionFactory(null, entrypointFileGenerator);
        Execution execution = goExecutionFactory.createExecution(file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.copyDockerFileToExecutionDirectory();
        
        // Then
        File dockerfileCopy = new File(execution.getPath() + "/" + DOCKERFILE);
        
        Assertions.assertTrue(dockerfileCopy.exists());
        Assertions.assertTrue(dockerfileCopy.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
}
