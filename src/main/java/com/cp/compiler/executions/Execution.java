package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import com.cp.compiler.templates.EntrypointFileGenerator;
import com.cp.compiler.utils.FileUtils;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
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
    
    private static final String IMAGE_PREFIX_NAME = "image-";
    
    private static final String EXECUTION_FOLDER_PREFIX_NAME = "execution-";
    
    @NonNull
    private MultipartFile sourceCodeFile;
    
    private MultipartFile inputFile;
    
    @NonNull
    private MultipartFile expectedOutputFile;
    
    @NonNull
    private int timeLimit;
    
    @NonNull
    private int memoryLimit;
    
    @Getter
    @NonNull
    private String id;
    
    @Getter
    /**
     * The Path of the execution directory
     */
    private String path;

    // For monitoring purpose it represents the number of executions in parallel for each programming language
    private final Counter executionCounter;
    
    @Getter
    private final EntrypointFileGenerator entrypointFileGenerator;
    
    /**
     * Instantiates a new Execution.
     *
     * @param sourceCodeFile          the source code
     * @param inputFile               the inputFile file
     * @param expectedOutputFile      the expected output file
     * @param timeLimit               the time limit
     * @param memoryLimit             the memory limit
     * @param executionCounter        the execution counter
     * @param entrypointFileGenerator the entrypointFile generator
     */
    protected Execution(MultipartFile sourceCodeFile,
                        MultipartFile inputFile,
                        MultipartFile expectedOutputFile,
                        int timeLimit,
                        int memoryLimit,
                        Counter executionCounter,
                        EntrypointFileGenerator entrypointFileGenerator) {
        this.sourceCodeFile = sourceCodeFile;
        this.inputFile = inputFile;
        this.expectedOutputFile = expectedOutputFile;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.executionCounter = executionCounter;
        this.entrypointFileGenerator = entrypointFileGenerator;
        this.id = UUID.randomUUID().toString();
        this.path = getLanguage().getFolderName() + "/" + getExecutionFolderName(); // this should come after the id inits
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
        log.debug("Copying execution Dockerfile to execution directory");
        copyDockerFilesToExecutionDirectory();
        log.debug("Creating an entrypoint file");
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
        String sourceCodeFileName = sourceCodeFile.getOriginalFilename();
        FileUtils.saveUploadedFiles(sourceCodeFile, path + "/" + sourceCodeFileName);
        FileUtils.saveUploadedFiles(expectedOutputFile, path + "/" + expectedOutputFile.getOriginalFilename());
        if (getInputFile() != null) {
            FileUtils.saveUploadedFiles(getInputFile(), path + "/" + inputFile.getOriginalFilename());
        }
    }
    
    /**
     * Gets image name.
     *
     * @return the image name
     */
    public String getImageName() {
        return IMAGE_PREFIX_NAME + id;
    }
    
    /**
     * Gets execution folder name.
     *
     * @return the execution folder name
     */
    public String getExecutionFolderName() {
        return EXECUTION_FOLDER_PREFIX_NAME + id;
    }
    
    /**
     * Copy docker file to execution directory.
     *
     * @throws IOException the io exception
     */
    protected void copyDockerFilesToExecutionDirectory() throws IOException {
        // Execution Dockerfile
        FileUtils.copyFile(getLanguage()
                                .getFolderName()
                                .concat("/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME),
                           path.concat("/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME));
    }
    
    /**
     * Creates entrypoint file
     */
    protected abstract void createEntrypointFile();
    
    /**
     * Get the language represented by the class
     *
     * @return the language
     */
    public abstract Language getLanguage();

}
