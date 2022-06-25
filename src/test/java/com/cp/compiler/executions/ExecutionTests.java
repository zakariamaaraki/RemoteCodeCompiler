package com.cp.compiler.executions;

import com.cp.compiler.executions.c.CExecutionFactory;
import com.cp.compiler.executions.cpp.CPPExecutionFactory;
import com.cp.compiler.executions.cs.CSExecutionFactory;
import com.cp.compiler.executions.go.GoExecutionFactory;
import com.cp.compiler.executions.haskell.HaskellExecutionFactory;
import com.cp.compiler.executions.java.JavaExecutionFactory;
import com.cp.compiler.executions.kotlin.KotlinExecutionFactory;
import com.cp.compiler.executions.python.PythonExecutionFactory;
import com.cp.compiler.executions.ruby.RubyExecutionFactory;
import com.cp.compiler.executions.rust.RustExecutionFactory;
import com.cp.compiler.executions.scala.ScalaExecutionFactory;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.WellKnownFiles;
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
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
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
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
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
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
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
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
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
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
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
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void scalaExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var scalaExecutionFactory = new ScalaExecutionFactory(null, entrypointFileGenerator);
        Execution execution = scalaExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
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
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void rustExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var rustExecutionFactory = new RustExecutionFactory(null, entrypointFileGenerator);
        Execution execution = rustExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void rubyExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var rubyExecutionFactory = new RubyExecutionFactory(null, entrypointFileGenerator);
        Execution execution = rubyExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void haskellExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        var haskellExecutionFactory = new HaskellExecutionFactory(null, entrypointFileGenerator);
        Execution execution = haskellExecutionFactory.createExecution(
                file, file, file, 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile();
        
        // Then
        File executionFolder = new File(execution.getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME);
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
