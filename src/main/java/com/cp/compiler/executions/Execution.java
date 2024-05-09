package com.cp.compiler.executions;

import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.Language;
import com.cp.compiler.templates.EntrypointFileGenerator;
import com.cp.compiler.utils.FileUtils;
import com.cp.compiler.consts.WellKnownFiles;
import com.cp.compiler.consts.WellKnownTemplates;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.core.instrument.Counter;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The abstract type Execution.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Getter
@EqualsAndHashCode
public abstract class Execution {

    private static final String IMAGE_PREFIX_NAME = "image-";

    private static final String EXECUTION_FOLDER_PREFIX_NAME = "execution-";

    public static final String EXECUTION_CONTAINER_NAME_PREFIX = "execution-";
    
    @NonNull
    @JsonIgnore
    private MultipartFile sourceCodeFile;
    
    @NonNull
    @JsonIgnore
    private List<TransformedTestCase> testCases;
    
    @NonNull
    private int timeLimit;
    
    @NonNull
    private int memoryLimit;
    
    @Getter
    @NonNull
    private String id;
    
    @NonNull
    @JsonIgnore
    private ExecutionType executionType;

    @Setter
    @Getter
    @NonNull
    private ExecutionState executionState;
    
    @Getter
    /**
     * The Path of the execution directory
     */
    private String path;

    @Getter
    private LocalDateTime dateTime;
    
    /**
     * Instantiates a new Execution.
     *
     * @param sourceCodeFile the source code
     * @param testCases      the test cases
     * @param timeLimit      the time limit
     * @param memoryLimit    the memory limit
     * @param executionType  the execution type
     */
    protected Execution(MultipartFile sourceCodeFile,
                        List<TransformedTestCase> testCases,
                        int timeLimit,
                        int memoryLimit,
                        ExecutionType executionType) {
        this.sourceCodeFile = sourceCodeFile;
        this.testCases = testCases;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.executionType = executionType;
        this.id = UUID.randomUUID().toString();
        this.path = getLanguage().getFolderName() + "/" + getExecutionFolderName(); // this should come after the id inits
        this.executionState = ExecutionState.NotStarted;
        this.dateTime = LocalDateTime.now();

        // register the execution
        this.executionType.getExecutionRepository().addExecution(this);
    }
    
    /**
     * Create execution directory.
     *
     * @throws IOException the io exception
     */
    public void createExecutionDirectory() throws IOException {
        this.executionState = ExecutionState.CreatingExecutionEnvironment;
        getExecutionCounter().increment();
        Files.createDirectory(Path.of(path));
        log.debug("Saving uploaded files");
        saveUploadedFiles();
        log.debug("Copying execution Dockerfile to execution directory");
        copyDockerFilesToExecutionDirectory();
        log.debug("Copying " + getLanguage() + " specific files");
        copyLanguageSpecificFilesToExecutionDirectory();
        this.executionState = ExecutionState.ExecutionEnvironmentReady;
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
        
        for (TransformedTestCase testCase : testCases) {
            if (testCase.getInputFile() != null) {
                FileUtils.saveUploadedFiles(
                        testCase.getInputFile(),
                        path + "/" + testCase.getInputFile().getOriginalFilename());
            }
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
     *
     * @param inputFileName the input file name
     * @param testCaseId    the test case id
     */
    @SneakyThrows
    public void createEntrypointFile(String inputFileName, String testCaseId) {
        
        String content = getEntrypointFileGenerator()
                .createEntrypointFile(WellKnownTemplates.ENTRYPOINT_TEMPLATE, getParameters(inputFileName));
    
        String path = getPath()
                + "/"
                + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                + testCaseId
                + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION;
    
        Files.deleteIfExists(Path.of(path));
    
        try(OutputStream os = new FileOutputStream(path)) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
    
    /**
     * Create entrypoint files.
     */
    public void createEntrypointFiles() {
        testCases.forEach(convertedTestCase -> {
            String testCaseId = convertedTestCase.getTestCaseId();
            String inputFileName = convertedTestCase.getInputFile() == null
                    ? null
                    : convertedTestCase.getInputFile().getOriginalFilename();
            log.info("Creating entrypoint file for test case id = {}", testCaseId);
            createEntrypointFile(inputFileName, testCaseId);
        });
    }
    
    /**
     * Gets counter.
     *
     * @return the counter
     */
    @JsonIgnore
    public Counter getExecutionCounter() {
        return executionType.getExecutionCounter();
    }
    
    /**
     * Gets entrypoint file generator.
     *
     * @return the entrypoint file generator
     */
    @JsonIgnore
    public EntrypointFileGenerator getEntrypointFileGenerator() {
        return executionType.getEntrypointFileGenerator();
    }

    public String getTestCaseContainerName(String testCaseId) {
        return EXECUTION_CONTAINER_NAME_PREFIX
                .concat(testCaseId)
                .concat("-")
                .concat(this.getImageName());
    }

    /**
     * Get the language represented by the class
     *
     * @return the language
     */
    public abstract Language getLanguage();
    
    
    /**
     * Gets parameters.
     * It is specific to each programming language, each one has it's one parameters and transformations
     *
     * @param inputFileName the input file name
     * @return the execution parameters
     */
    protected abstract Map<String, String> getParameters(String inputFileName);
    
    /**
     * Copy language specific files to execution directory.
     *
     * @throws IOException the io exception
     */
    protected abstract void copyLanguageSpecificFilesToExecutionDirectory() throws IOException;
}
