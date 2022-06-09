package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import com.cp.compiler.utilities.FilesUtil;
import io.micrometer.core.instrument.Counter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * The type Execution.
 */
@Slf4j
@Getter
@EqualsAndHashCode
public abstract class Execution {
    
    /**
     * The constant TIMEOUT_CMD.
     */
    protected static final String TIMEOUT_CMD = "timeout --signal=SIGTERM ";
    /**
     * The constant BASH_HEADER.
     */
    protected static final String BASH_HEADER = "#!/usr/bin/env bash\n";
    
    @NonNull
    private MultipartFile sourceCodeFile;
    
    private MultipartFile inputFile;
    
    @NonNull
    private MultipartFile expectedOutputFile;
    
    @NonNull
    private int timeLimit;
    
    @NonNull
    private int memoryLimit;
    
    @NonNull
    private String imageName;
    
    @Getter
    /**
     * The Path of the execution directory
     */
    private String path;
    
    @Getter
    /**
     * The Language.
     */
    private Language language;
    
    // For monitoring purpose it represents the number of executions in parallel for each programming language
    private final Counter executionCounter;
    
    /**
     * Instantiates a new Execution.
     *
     * @param sourceCodeFile     the source code
     * @param inputFile          the inputFile file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    protected Execution(MultipartFile sourceCodeFile,
                        MultipartFile inputFile,
                        MultipartFile expectedOutputFile,
                        int timeLimit,
                        int memoryLimit,
                        Language language,
                        Counter executionCounter) {
        this.sourceCodeFile = sourceCodeFile;
        this.inputFile = inputFile;
        this.expectedOutputFile = expectedOutputFile;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.language = language;
        this.executionCounter = executionCounter;
        this.imageName = UUID.randomUUID().toString();
        this.path = language.getFolder() + "/" + imageName;
    }
    
    /**
     * Create execution directory.
     *
     * @throws IOException the io exception
     */
    public void createExecutionDirectory() throws IOException {
        executionCounter.increment();
        Files.createDirectory(Path.of(path));
        log.debug("Saving uploaded files");
        saveUploadedFiles();
        log.debug("Copying Dockerfile to execution directory");
        copyDockerFileToExecutionDirectory();
        createEntrypointFile();
    }
    
    /**
     * Delete execution directory.
     *
     * @throws IOException the io exception
     */
    public void deleteExecutionDirectory() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of(path));
    }
    
    /**
     * Save uploaded files.
     *
     * @throws IOException the io exception
     */
    protected void saveUploadedFiles() throws IOException {
        FilesUtil.saveUploadedFiles(sourceCodeFile, path + "/" + language.getFile());
        FilesUtil.saveUploadedFiles(
                expectedOutputFile, path + "/" + expectedOutputFile.getOriginalFilename());
        if (getInputFile() != null) {
            FilesUtil.saveUploadedFiles(getInputFile(), path + "/" + inputFile.getOriginalFilename());
        }
    }
    
    /**
     * Copy docker file to execution directory.
     *
     * @throws IOException the io exception
     */
    protected void copyDockerFileToExecutionDirectory() throws IOException {
        FilesUtil.copyFile(language.getFolder().concat("/Dockerfile"), path.concat("/Dockerfile"));
    }
    
    /**
     * Creates entrypoint file
     */
    protected abstract void createEntrypointFile();
    
}
