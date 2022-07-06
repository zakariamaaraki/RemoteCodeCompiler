package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
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
        AbstractExecutionFactory javaExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new JavaExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory pythonExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new PythonExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory cExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new CExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory cppExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new CPPExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory csExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new CSExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory kotlinExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new KotlinExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory scalaExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new ScalaExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory goExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new GoExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory rustExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new RustExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory rubyExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new RubyExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory haskellExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new HaskellExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
        AbstractExecutionFactory goExecutionFactory =
                (MultipartFile sourceCode, MultipartFile inputFile, MultipartFile expectedOutputFile, int timeLimit, int memoryLimit) -> {
                    return new GoExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            null,
                            entrypointFileGenerator);
                };
        
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
