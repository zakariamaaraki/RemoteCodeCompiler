package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import com.cp.compiler.utilities.FilesUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * The type Execution.
 */
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
    
    private MultipartFile sourceCodeFile;
    private MultipartFile inputFile;
    private MultipartFile expectedOutputFile;
    private int timeLimit;
    private int memoryLimit;
    private String imageName;
    /**
     * The Path of the execution directory
     */
    protected String path;
    
    /**
     * Instantiates a new Execution.
     *
     * @param sourceCodeFile         the source code
     * @param inputFile          the inputFile file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     */
    protected Execution(MultipartFile sourceCodeFile,
                     MultipartFile inputFile,
                     MultipartFile expectedOutputFile,
                     int timeLimit,
                     int memoryLimit) {
        this.sourceCodeFile = sourceCodeFile;
        this.inputFile = inputFile;
        this.expectedOutputFile = expectedOutputFile;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.imageName = UUID.randomUUID().toString();
    }
    
    public void createExecutionDirectory() throws IOException {
        Files.createDirectory(Path.of(path));
        saveUploadedFiles();
        copyDockerFileToExecutionDirectory();
        createEntrypointFile();
    }
    
    public void deleteExecutionDirectory() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of(path));
    }
    
    /**
     * Sets the path of the execution directory
     *
     * @param language the language
     */
    protected void setpath(Language language) {
        path = language.getFolder() + "/" + imageName;
    }
    
    /**
     * Save uploaded files.
     *
     * @param language the language
     * @throws IOException the io exception
     */
    protected void saveUploadedFiles(Language language) throws IOException {
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
     * @param language the language
     * @throws IOException the io exception
     */
    protected void copyDockerFileToExecutionDirectory(Language language) throws IOException {
        FilesUtil.copyFile(language.getFolder().concat("/Dockerfile"), path.concat("/Dockerfile"));
    }
    
    /**
     * Creates entrypoint file
     */
    protected abstract void createEntrypointFile();
    
    /**
     * Save uploaded files.
     *
     * @throws IOException the io exception
     */
    protected abstract void saveUploadedFiles() throws IOException;
    
    /**
     * Copy docker file to execution directory.
     *
     * @throws IOException the io exception
     */
    protected abstract void copyDockerFileToExecutionDirectory() throws IOException;
}
