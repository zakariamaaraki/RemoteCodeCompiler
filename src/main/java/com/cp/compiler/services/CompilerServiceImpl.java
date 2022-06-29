package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerServerInternalException;
import com.cp.compiler.exceptions.ContainerBuildException;
import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.*;
import com.cp.compiler.utilities.CmdUtil;
import com.cp.compiler.utilities.StatusUtil;
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
import java.util.Map;

/**
 * Compiler Service Class, this class provides compilation utilities for several programing languages
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Service("client")
public class CompilerServiceImpl implements CompilerService {
    
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
    public CompilerServiceImpl(ContainerService containerService, MeterRegistry meterRegistry) {
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
    public ResponseEntity compile(Execution execution) throws Exception {
        
        LocalDateTime dateTime = LocalDateTime.now();
    
        builderImage(execution);
        
        Result result = runCode(execution.getImageName(), execution.getExpectedOutputFile());
        
        if (deleteDockerImage) {
            try {
                containerService.deleteImage(execution.getImageName());
                log.info("Image " + execution.getImageName() + " has been deleted");
            } catch (Exception e) {
                log.warn("Error, can't delete image {} : {}", execution.getImageName(), e);
            }
        }
        
        log.info("Status response is " + result.getStatusResponse());
        
        // update metrics
        verdictsCounters.get(result.getStatusResponse()).increment();
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(result, dateTime));
    }
    
    private Result runCode(String imageName, MultipartFile outputFile) {
        try {
            BufferedReader expectedOutputReader =
                    new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
            String expectedOutput = CmdUtil.readOutput(expectedOutputReader);
            
            ContainerOutput containerOutput = containerService.runContainer(imageName, TIME_OUT);
            
            Verdict verdict = getVerdict(containerOutput, expectedOutput);
            
            return new Result(
                    verdict,
                    containerOutput.getStdOut(),
                    containerOutput.getStdErr(),
                    expectedOutput,
                    containerOutput.getExecutionDuration());
            
        } catch (Exception e) {
            log.error("Error on the container engine side: {}", e);
            throw new ContainerFailedDependencyException("Error on the container engine side: " + e.getMessage());
        }
    }
    
    private Verdict getVerdict(ContainerOutput containerOutput, String expectedOutput) {
        boolean result = CmdUtil.compareOutput(containerOutput.getStdOut(), expectedOutput);
        return StatusUtil.statusResponse(containerOutput.getStatus(), result);
    }
    
    private void builderImage(Execution execution) throws CompilerServerInternalException {
        try {
            log.info("Creating execution directory: {}", execution.getImageName());
            execution.createExecutionDirectory();
        } catch (Throwable e) {
            throw new CompilerServerInternalException(e.getMessage());
        }
        
        try {
            log.info("Building the docker image: {}", execution.getImageName());
            int status = containerService.buildImage(execution.getPath(), execution.getImageName());
            if (status == 0) {
                log.info("Container image has been built");
            } else {
                log.warn("Error while building container image");
                throw new ContainerBuildException("Error while building container image, Status Code : "
                        + status);
            }
        } finally {
            try {
                execution.deleteExecutionDirectory();
                log.info("Execution directory {} has been deleted", execution.getImageName());
            } catch (IOException e) {
                log.warn("Error while trying to delete execution directory, {}", e);
            }
        }
    }
}
