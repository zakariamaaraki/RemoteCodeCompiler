package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerServerInternalException;
import com.cp.compiler.exceptions.ContainerBuildException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Result;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.models.WellKnownMetrics;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
        
        Result result = containerService.runCode(
                execution.getImageName(), execution.getExpectedOutputFile(), execution.getTimeLimit());
        
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
