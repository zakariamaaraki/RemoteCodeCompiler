package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerServerInternalException;
import com.cp.compiler.exceptions.ContainerBuildException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Result;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Compiler Service Class, this class provides compilation utilities for several programing languages
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Service("client")
public class CompilerServiceImpl implements CompilerService {
    
    private final ContainerService containerService;

    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;
    
    public CompilerServiceImpl(ContainerService containerService) {
        this.containerService = containerService;
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
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(result, dateTime));
    }
    
    private void builderImage(Execution execution) throws CompilerServerInternalException {
        try {
            log.info("Creating execution directory");
            execution.createExecutionDirectory();
        } catch (Exception e) {
            throw new CompilerServerInternalException(e.getMessage());
        }
        
        try {
            log.info("Building the docker image");
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
                log.info("Execution directory has been deleted");
            } catch (IOException e) {
                log.warn("Error while trying to delete execution directory, {}", e);
            }
        }
    }
}
