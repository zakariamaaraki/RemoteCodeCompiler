package com.cp.compiler.services;

import com.cp.compiler.exceptions.*;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.*;
import com.cp.compiler.utils.CmdUtils;
import com.cp.compiler.utils.StatusUtils;
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
    
    private final ContainerService containerService;
    
    private final MeterRegistry meterRegistry;
    
    private final Map<String, Counter> verdictsCounters = new HashMap<>();

    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;
    
    /**
     * Instantiates a new Compiler service.
     *
     * @param containerService the container service
     * @param meterRegistry    the meter registry
     */
    public CompilerServiceDefault(ContainerService containerService, MeterRegistry meterRegistry) {
        this.containerService = containerService;
        this.meterRegistry = meterRegistry;
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
    public ResponseEntity compile(Execution execution) {
        
        LocalDateTime dateTime = LocalDateTime.now();
    
        builderImage(execution);
        
        Result result = runCode(execution.getImageName(), execution.getExpectedOutputFile());
        
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
    }
    
    private Result runCode(String imageName, MultipartFile outputFile) {
    
        BufferedReader expectedOutputReader;
        String expectedOutput;
        try {
            expectedOutputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
            expectedOutput = CmdUtils.readOutput(expectedOutputReader);
        } catch (Exception e) {
            throw new CompilerServerInternalException("Unexpected error while reading the expected output file");
        }
    
        ProcessOutput containerOutput;
        try {
            containerOutput = containerService.runContainer(imageName, TIME_OUT);
            Verdict verdict = getVerdict(containerOutput, expectedOutput);
        
            return new Result(
                    verdict,
                    containerOutput.getStdOut(),
                    containerOutput.getStdErr(),
                    expectedOutput,
                    containerOutput.getExecutionDuration());
        
        } catch(ContainerOperationTimeoutException exception) {
            log.info("{}", exception);
            return new Result(
                    Verdict.TIME_LIMIT_EXCEEDED,
                    "",
                    "The execution exceeded the time limit",
                    expectedOutput,
                    0);
        }
    }
    
    private Verdict getVerdict(ProcessOutput containerOutput, String expectedOutput) {
        boolean result = CmdUtils.compareOutput(containerOutput.getStdOut(), expectedOutput);
        return StatusUtils.statusResponse(containerOutput.getStatus(), result);
    }
    
    private void builderImage(Execution execution) {
        try {
            log.info("Creating execution directory: {}", execution.getExecutionFolderName());
            execution.createExecutionDirectory();
        } catch (Throwable e) {
            throw new CompilerServerInternalException(e.getMessage());
        }
        
        try {
            log.info("Building the docker image: {}", execution.getImageName());
            try {
                String buildLogs = containerService.buildImage(execution.getPath(), execution.getImageName());
                log.debug(buildLogs);
                log.info("Container image has been built");
            } catch(ContainerFailedDependencyException exception) {
                log.warn("Error while building container image: {}", exception);
                throw new ContainerBuildException("Error while building container image: " + exception.getMessage());
            }
        } finally {
            try {
                execution.deleteExecutionDirectory();
                log.info("Execution directory {} has been deleted", execution.getExecutionFolderName());
            } catch (IOException e) {
                log.warn("Error while trying to delete execution directory, {}", e);
            }
        }
    }
}
