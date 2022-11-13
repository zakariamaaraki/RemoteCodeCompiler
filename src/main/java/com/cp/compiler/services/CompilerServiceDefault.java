package com.cp.compiler.services;

import com.cp.compiler.exceptions.*;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.*;
import com.cp.compiler.utils.CmdUtils;
import com.cp.compiler.utils.StatusUtils;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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
import java.util.Map;

/**
 * Compiler Service Class, this class provides compilation utilities for several programing languages
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Service("client")
public class CompilerServiceDefault implements CompilerService {
    
    private static final long TIME_OUT = 20000; // in ms
    
    // Note: this value should not be updated, once update don't forget to update build.sh script used to build these images.
    private static final String IMAGE_PREFIX_NAME = "compiler.";
    
    // Note: this value should not be updated, once update don't forget to update it also in all compilation Dockerfiles.
    private static final String EXECUTION_PATH_INSIDE_CONTAINER = "/app";
    
    private final ContainerService containerService;
    
    private final MeterRegistry meterRegistry;
    
    private final Map<String, Counter> verdictsCounters = new HashMap<>();

    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;
    
    @Value("${compiler.compilation-container.volume:}")
    private String compilationContainerVolume;
    
    private final Resources resources;
    
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
    }
    
    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        Arrays.stream(Verdict.values())
                .forEach(verdict -> verdictsCounters.put(verdict.getStatusResponse(),
                        meterRegistry.counter(verdict.getCounterMetric())));
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
            String expectedOutput = getExpectedOutput(execution.getExpectedOutputFile());
            
            // 2- Compile the source code if the language is a compiled language
            if (execution.getLanguage().isCompiled()) {
                String compilationImageName = IMAGE_PREFIX_NAME + execution.getLanguage().toString().toLowerCase(); // repository name must be lowercase
                // If the app is running inside a container, we should share the same volume with the compilation container.
                final String volume = compilationContainerVolume.isEmpty()
                                            ? System.getProperty("user.dir")
                                            : compilationContainerVolume;

                String sourceCodeFileName = execution.getSourceCodeFile().getOriginalFilename();
                ProcessOutput processOutput =
                        compile(volume, compilationImageName, execution.getPath(), sourceCodeFileName);
                
                if (processOutput.getStatus() != StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS) {

                    cleanStdErrOutput(processOutput, execution);
                    Result result = new Result(
                            Verdict.COMPILATION_ERROR,
                            "",
                            processOutput.getStdErr(),
                            expectedOutput,
                            0);
                    
                    // Will return compilation Error Status
                    return ResponseEntity.ok(new Response(result, dateTime));
                }
                log.info("Compilation succeeded!");
            }
            
            // 2 - Build the execution container
            buildContainerImage(execution.getPath(), execution.getImageName(), WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
            
            // 3 - Run the execution container
            Result result = runContainer(execution.getImageName(), expectedOutput);
    
            // 4 - Clean up
            if (deleteDockerImage) {
                try {
                    containerService.deleteImage(execution.getImageName());
                    log.info("Image {} has been deleted", execution.getImageName());
                } catch (Exception e) {
                    log.warn("Error, can't delete image {} : {}", execution.getImageName(), e);
                }
            }
    
            log.info("Status response is {}", result.getStatusResponse());
    
            // Update metrics
            verdictsCounters.get(result.getStatusResponse()).increment();
    
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(result, dateTime));
        } finally {
            deleteExecutionEnvironment(execution);
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
        return containerService.runContainer(imageName, TIME_OUT, volumeMounting, executionPath, sourceCodeFileName);
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
    
    private Result runContainer(String imageName, String expectedOutput) {
        
        try {
            ProcessOutput containerOutput = containerService.runContainer(imageName, TIME_OUT, resources.getMaxCpus());
            Verdict verdict = getVerdict(containerOutput, expectedOutput);
        
            // TODO: clean stderr output
            
            return new Result(
                    verdict,
                    containerOutput.getStdOut(),
                    containerOutput.getStdErr(),
                    expectedOutput,
                    containerOutput.getExecutionDuration());
        
        } catch(ContainerOperationTimeoutException exception) {
            log.warn("Tme limit exceeded during the execution: {}", exception); // Should be caught inside the container
            // TODO: set the correct value of time limit, the one given by the user
            return new Result(
                    Verdict.TIME_LIMIT_EXCEEDED,
                    "",
                    "The execution exceeded the time limit",
                    expectedOutput,
                    TIME_OUT);
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
        } catch(ContainerFailedDependencyException exception) {
            log.warn("Error while building container image: {}", exception);
            throw new ContainerBuildException("Error while building compilation image: " + exception.getMessage());
        }
    }
}
