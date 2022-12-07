package com.cp.compiler.services.businesslogic;

import com.cp.compiler.exceptions.*;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.*;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.testcases.TestCaseResult;
import com.cp.compiler.services.containers.ContainerService;
import com.cp.compiler.services.resources.Resources;
import com.cp.compiler.utils.CmdUtils;
import com.cp.compiler.utils.StatusUtils;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.cp.compiler.wellknownconstants.WellKnownMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Compiler Service Class, this class provides compilation utilities for several programing languages
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Service("client")
public class CompilerServiceDefault implements CompilerService {
    
    // Note: changing this value is critical, it has a lot of impact on the compilation step
    private static final long COMPILATION_TIME_OUT = 60000; // in ms
    
    private static final long EXECUTION_TIME_OUT = 20000; // in ms
    
    // Note: this value should not be updated, once update don't forget to update build.sh script used to build these images.
    private static final String IMAGE_PREFIX_NAME = "compiler.";
    
    // Note: this value should not be updated, once update don't forget to update it also in all compilation Dockerfiles.
    private static final String EXECUTION_PATH_INSIDE_CONTAINER = "/app";
    
    private final ContainerService containerService;
    
    private final MeterRegistry meterRegistry;
    
    private Timer compilationTimer;
    
    private Timer executionTimer;
    
    private final Map<String, Counter> verdictsCounters = new HashMap<>();

    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;
    
    @Value("${compiler.compilation-container.volume:}")
    private String compilationContainerVolume;
    
    private final Resources resources;
    
    private final ExecutorService threadPool;
    
    /**
     * Instantiates a new Compiler service.
     *  @param containerService the container service
     * @param meterRegistry    the meter registry
     * @param resources
     */
    public CompilerServiceDefault(ContainerService containerService,
                                  MeterRegistry meterRegistry,
                                  Resources resources) {
        this.containerService = containerService;
        this.meterRegistry = meterRegistry;
        this.resources = resources;
        this.threadPool = Executors.newCachedThreadPool();
    }
    
    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        Arrays.stream(Verdict.values())
                .forEach(verdict -> verdictsCounters.put(verdict.getStatusResponse(),
                        meterRegistry.counter(verdict.getCounterMetric())));
        
        compilationTimer = meterRegistry.timer(WellKnownMetrics.COMPILATION_TIMER, "compiler", "compilation");
        executionTimer = meterRegistry.timer(WellKnownMetrics.EXECUTION_TIMER, "compiler", "execution");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity execute(Execution execution) {
        LocalDateTime dateTime = LocalDateTime.now();
        // 1- Build execution environment (create directory, upload files, ...)
        buildExecutionEnvironment(execution);
        
        try {
            int compilationDuration = 0;
            
            // 2- Compile the source code if the language is a compiled language
            if (execution.getLanguage().isCompiled()) {
                
                String compilationImageName = IMAGE_PREFIX_NAME + execution.getLanguage().toString().toLowerCase(); // repository name must be lowercase
                // If the app is running inside a container, we should share the same volume with the compilation container.
                final String volume = compilationContainerVolume.isEmpty()
                                            ? System.getProperty("user.dir")
                                            : compilationContainerVolume;

                String sourceCodeFileName = execution.getSourceCodeFile().getOriginalFilename();
    
                var processOutput = new AtomicReference<ProcessOutput>();
                compilationTimer.record(() -> {
                     processOutput.set(compile(volume, compilationImageName, execution.getPath(), sourceCodeFileName));
                });
                compilationDuration = processOutput.get().getExecutionDuration();
                
                if (processOutput.get().getStatus() != StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS) {

                    cleanStdErrOutput(processOutput.get(), execution);
    
                    var response = new Response(
                            Verdict.COMPILATION_ERROR.getStatusResponse(),
                            Verdict.COMPILATION_ERROR.getStatusCode(),
                            processOutput.get().getStdErr(),
                            new LinkedHashMap<>(),
                            compilationDuration,
                            execution.getTimeLimit(),
                            execution.getMemoryLimit(),
                            execution.getLanguage(),
                            dateTime);
                    
                    // Will return a compilation Error verdict
                    return ResponseEntity.ok(response);
                }
                log.info("Compilation succeeded!");
            }
            
            var testCasesResult = new LinkedHashMap<String, TestCaseResult>();
            Verdict verdict = null;
            String err = "";
            
            for (ConvertedTestCase testCase : execution.getTestCases()) {
                
                // Create an entrypoint file for the current execution
                execution.createEntrypointFile(
                        testCase.getInputFile() == null
                                ? null
                                : testCase.getInputFile().getOriginalFilename());
    
                String expectedOutput = getExpectedOutput(testCase.getExpectedOutputFile());
                
                // Free memory space
                testCase.freeMemorySpace();
                
                var result = new AtomicReference<TestCaseResult>();
                executionTimer.record(() -> {
                    // 2 - Build the execution container
                    buildContainerImage(
                            execution.getPath(),
                            execution.getImageName(),
                            WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
                    // 3 - Run the execution container
                    result.set(runContainer(execution, expectedOutput));
                });
                
                // 4 - Delete container image asynchronously
                if (deleteDockerImage) {
                    threadPool.execute(() -> {
                        try {
                            containerService.deleteImage(execution.getImageName());
                            log.info("Image {} has been deleted", execution.getImageName());
                        } catch (Exception e) {
                            if (e instanceof ContainerOperationTimeoutException) {
                                log.warn("Timeout, didn't get the response at time from container engine if the image {} was deleted",
                                        execution.getImageName());
                            } else {
                                log.warn("Error, can't delete image {} : {}", execution.getImageName(), e);
                            }
                        }
                    });
                }
                
                TestCaseResult testCaseResult = result.get();
    
                testCasesResult.put(testCase.getTestCaseId(), testCaseResult);
                verdict = testCaseResult.getVerdict();
            
                log.info("Status response for the test case {} is {}",
                        testCase.getTestCaseId(),
                        verdict.getStatusResponse());
    
                // Update metrics
                verdictsCounters.get(verdict.getStatusResponse()).increment();
                
                if (verdict != Verdict.ACCEPTED) {
                    // Don't continue if the current test case failed
                    log.info("Test case id: {} failed, abort executions", testCase.getTestCaseId());
                    err = testCaseResult.getError();
                    break;
                }
            }
    
            log.info("Execution finished, the verdict is {}", verdict.getStatusResponse());
            
            var response = new Response(
                    verdict.getStatusResponse(),
                    verdict.getStatusCode(),
                    err,
                    testCasesResult,
                    compilationDuration,
                    execution.getTimeLimit(),
                    execution.getMemoryLimit(),
                    execution.getLanguage(),
                    dateTime);
    
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        } finally {
            // 5 - Clean up asynchronously
            threadPool.execute(() -> deleteExecutionEnvironment(execution));
        }
    }
    
    private void cleanStdErrOutput(ProcessOutput processOutput, Execution execution) {
        // Don't return the absolut path to the user
        processOutput.setStdErr(processOutput.getStdErr().replace(execution.getPath(), ""));
    }
    
    private void buildExecutionEnvironment(Execution execution) {
        try {
            log.info("Creating execution directory: {}", execution.getExecutionFolderName());
            execution.createExecutionDirectory();
        } catch (Throwable e) {
            log.error("Error while building execution environment: {}", e);
            throw new CompilerServerInternalException(e.getMessage());
        }
    }
    
    private void deleteExecutionEnvironment(Execution execution) {
        try {
            execution.deleteExecutionDirectory();
            log.info("Execution directory {} has been deleted", execution.getExecutionFolderName());
        } catch (IOException e) {
            log.warn("Error while trying to delete execution directory, {}", e);
        }
    }
    
    private ProcessOutput compile(String volume, String imageName, String executionPath, String sourceCodeFileName) {
        String volumeMounting = volume + ":" + EXECUTION_PATH_INSIDE_CONTAINER;
        return containerService.runContainer(
                imageName,
                COMPILATION_TIME_OUT,
                volumeMounting,
                executionPath,
                sourceCodeFileName);
    }
    
    private String getExpectedOutput(MultipartFile outputFile) {
        try {
            var expectedOutputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
            return CmdUtils.readOutput(expectedOutputReader);
        } catch (Exception exception) {
            log.error("Unexpected error while reading the expected output file: {}", exception);
            throw new CompilerServerInternalException("Unexpected error while reading the expected output file");
        }
    }
    
    private TestCaseResult runContainer(Execution execution, String expectedOutput) {
        
        try {
            ProcessOutput containerOutput =
                    containerService.runContainer(execution.getImageName(), EXECUTION_TIME_OUT, resources.getMaxCpus());
            Verdict verdict = getVerdict(containerOutput, expectedOutput);
        
            cleanStdErrOutput(containerOutput, execution);
            
            return new TestCaseResult(
                    verdict,
                    containerOutput.getStdOut(),
                    containerOutput.getStdErr(),
                    expectedOutput,
                    containerOutput.getExecutionDuration());
        
        } catch(ContainerOperationTimeoutException exception) {
            // Should be caught inside the container
            log.warn("Tme limit exceeded during the execution: {}", exception);
            return new TestCaseResult(
                    Verdict.TIME_LIMIT_EXCEEDED,
                    "",
                    "The execution exceeded the time limit",
                    expectedOutput,
                    execution.getTimeLimit() + 1);
        }
    }
    
    private Verdict getVerdict(ProcessOutput containerOutput, String expectedOutput) {
        boolean result = CmdUtils.compareOutput(containerOutput.getStdOut(), expectedOutput);
        return StatusUtils.statusResponse(containerOutput.getStatus(), result);
    }
    
    private void  buildContainerImage(String path, String imageName, String dockerfileName) {
        log.info("Start building the docker image: {}", imageName);
        try {
            String buildLogs = containerService.buildImage(path, imageName, dockerfileName);
            log.debug("Build logs: {}", buildLogs);
        } catch(Exception exception) {
            log.warn("Error while building container image: {}", exception);
            throw new ContainerBuildException("Error while building compilation image: " + exception.getMessage());
        }
    }
}
