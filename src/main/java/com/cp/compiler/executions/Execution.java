package com.cp.compiler.executions;

import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import com.cp.compiler.templates.EntrypointFileGenerator;
import com.cp.compiler.utils.FileUtils;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.cp.compiler.wellknownconstants.WellKnownTemplates;
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
import java.util.List;
import java.util.Map;
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
    
    @NonNull
    private List<ConvertedTestCase> testCases;
    
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
     * @param testCases               the test cases
     * @param timeLimit               the time limit
     * @param memoryLimit             the memory limit
     * @param executionCounter        the execution counter
     * @param entrypointFileGenerator the entrypointFile generator
     */
    protected Execution(MultipartFile sourceCodeFile,
                        List<ConvertedTestCase> testCases,
                        int timeLimit,
                        int memoryLimit,
                        Counter executionCounter,
                        EntrypointFileGenerator entrypointFileGenerator) {
        this.sourceCodeFile = sourceCodeFile;
        this.testCases = testCases;
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
        
        for (ConvertedTestCase testCase : testCases) {
            FileUtils.saveUploadedFiles(
                    testCase.getExpectedOutputFile(),
                    path + "/" + testCase.getExpectedOutputFile().getOriginalFilename());
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
     * Gets parameters.
     * It is specific to each programming language, each one has it's one parameters and transformations
     *
     * @param inputFileName the input file name
     * @return the execution parameters
     */
    protected abstract Map<String, String> getParameters(String inputFileName);
    
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
     * Get the language represented by the class
     *
     * @return the language
     */
    public abstract Language getLanguage();

}
