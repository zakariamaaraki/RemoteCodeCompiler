package com.cp.compiler.utilities;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test Execution directory creation
 */
class ExecutionTests {
    
    /**
     * Should create execution directory
     *
     * @throws IOException the io exception
     */
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
    
    /**
     * Should create an entrypoint file.
     *
     * @throws IOException the io exception
     */
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
                file, file, file, 0, 0, Language.CPP);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(execution.getPath() + "/entrypoint.sh")));
        execution.deleteExecutionDirectory();
    }
    
    /**
     * Should copy docker file.
     *
     * @throws IOException the io exception
     */
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
                file, file, file, 0, 0, Language.JAVA);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(execution.getPath() + "/Dockerfile")));
        execution.deleteExecutionDirectory();
    }
    
    /**
     * Delete execution directory should delete the directory.
     *
     * @throws IOException the io exception
     */
    @Test
    void deleteExecutionDirectoryShouldDeleteTheDirectory() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        
        // When
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 0, 0, Language.JAVA);
        
        // When
        execution.createExecutionDirectory();
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(execution.getPath())));
        execution.deleteExecutionDirectory();
        Assertions.assertFalse(Files.exists(Path.of(execution.getPath())));
    }
}
