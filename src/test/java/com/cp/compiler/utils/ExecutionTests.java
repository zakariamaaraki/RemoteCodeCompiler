package com.cp.compiler.utils;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
class ExecutionTests {
    
    @Test
    void shouldCreateExecutionDirectory() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    
        // When
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 0, 0, Language.PYTHON);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(execution.getPath())));
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void shouldCreateAnEntrypointFile() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        
        // When
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 100, Language.CPP);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(execution.getPath() + "/entrypoint.sh")));
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void shouldCopyDockerFile() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        
        // When
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 100, Language.PYTHON);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(execution.getPath() + "/Dockerfile")));
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void shouldDeleteTheDirectory() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 100, Language.JAVA);

        execution.createExecutionDirectory();
        
        // When
        Assertions.assertTrue(Files.exists(Path.of(execution.getPath())));
        execution.deleteExecutionDirectory();
        
        // Then
        Assertions.assertFalse(Files.exists(Path.of(execution.getPath())));
    }
}
