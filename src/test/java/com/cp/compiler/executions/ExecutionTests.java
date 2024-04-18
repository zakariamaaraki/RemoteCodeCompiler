package com.cp.compiler.executions;

import com.cp.compiler.executions.languages.*;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.Language;
import com.cp.compiler.consts.WellKnownFiles;
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
import java.util.List;

@DirtiesContext
@SpringBootTest
class ExecutionTests {
    
    @Autowired
    private EntrypointFileGenerator entrypointFileGenerator;
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void shouldCreateAnExecutionEnvironment() throws IOException {
        // Given
        var testCase = new TransformedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
        
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
        var testCase = new TransformedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
        
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
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = javaExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
    
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
    
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
    
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void pythonExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory pythonExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new PythonExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
        
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = pythonExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void cExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory cExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new CExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = cExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void cppExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory cppExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new CPPExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = cppExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void csExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory csExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new CSExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = csExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void kotlinExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory kotlinExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new KotlinExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = kotlinExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void scalaExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory scalaExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new ScalaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = scalaExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void goExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory goExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new GoExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = goExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void rustExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory rustExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new RustExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = rustExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void rubyExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory rubyExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new RubyExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = rubyExecutionFactory.createExecution(
                file, List.of(testCase) ,10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void haskellExecutionShouldCreateAnEntrypointFile() throws IOException {
        // Given
        AbstractExecutionFactory haskellExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new HaskellExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = haskellExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.createEntrypointFile(testCase.getInputFile().getOriginalFilename(), "id");
        
        // Then
        File executionFolder =
                new File(execution.getPath()
                        + "/"
                        + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                        + "id"
                        + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION);
        Assertions.assertTrue(executionFolder.exists());
        Assertions.assertTrue(executionFolder.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void shouldCopyExecutionDockerFileToExecutionDirectory() throws IOException {
        // Given
        AbstractExecutionFactory goExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new GoExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = goExecutionFactory.createExecution(file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.copyDockerFilesToExecutionDirectory();
        
        // Then
        File executionDockerfileCopy =
                new File(execution.getPath() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
        Assertions.assertTrue(executionDockerfileCopy.exists());
        Assertions.assertTrue(executionDockerfileCopy.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
    
    @Test
    void shouldCopyJavaSecurityPolicyFileToExecutionDirectory() throws IOException {
        // Given
        AbstractExecutionFactory javaExecutionFactory =
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit);
        
        var testCase = new TransformedTestCase("id", file, "test");
        
        Execution execution = javaExecutionFactory.createExecution(file, List.of(testCase), 10, 500);
        
        Files.createDirectory(Path.of(execution.getPath()));
        
        // When
        execution.copyLanguageSpecificFilesToExecutionDirectory();
        
        // Then
        File javaSecurityPolicyFile =
                new File(execution.getPath() + "/" + WellKnownFiles.JAVA_SECURITY_POLICY_FILE_NAME);
        
        Assertions.assertTrue(javaSecurityPolicyFile.exists());
        Assertions.assertTrue(javaSecurityPolicyFile.isFile());
        
        // Clean up
        execution.deleteExecutionDirectory();
    }
}
