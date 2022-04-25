package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerServerException;
import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Request;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Result;
import lombok.Getter;
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
@Service
public class CompilerServiceImpl implements CompilerService {
    
    private final ContainerService containerService;
    
    @Getter
    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;
    
    @Getter
    @Value("${compiler.execution-memory.max:10000}")
    private int maxExecutionMemory;
    
    @Getter
    @Value("${compiler.execution-memory.min:0}")
    private int minExecutionMemory;
    
    @Getter
    @Value("${compiler.execution-time.max:15}")
    private int maxExecutionTime;
    
    @Getter
    @Value("${compiler.execution-time.min:0}")
    private int minExecutionTime;
    
    public CompilerServiceImpl(ContainerService containerService) {
        this.containerService = containerService;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> compile(Request request) throws CompilerServerException, IOException {
        Execution execution = ExecutionFactory.createExecution(request.getExpectedOutput(),
                                                            request.getSourceCode(),
                                                            request.getInput(),
                                                            request.getTimeLimit(),
                                                            request.getMemoryLimit(),
                                                            request.getLanguage());
        return compile(execution);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> compile(Execution execution) throws CompilerServerException {
        
        LocalDateTime date = LocalDateTime.now();
        
        if (execution.getMemoryLimit() < minExecutionMemory || execution.getMemoryLimit() > maxExecutionMemory) {
            log.info(execution.getImageName() + " Error memoryLimit must be between {}Mb and {}Mb, provided : {}",
                     minExecutionMemory,
                     maxExecutionMemory,
                     execution.getTimeLimit());
            
            return ResponseEntity
                    .badRequest()
                    .body("Error memoryLimit must be between "
                            + minExecutionMemory + "Mb and " + maxExecutionMemory + "Mb, provided : "
                            + execution.getMemoryLimit());
        }
        
        
        if (execution.getTimeLimit() < minExecutionTime || execution.getTimeLimit() > maxExecutionTime) {
            log.info(execution.getImageName() + " Error timeLimit must be between {} Sec and {} Sec, provided : {}",
                     minExecutionTime,
                     maxExecutionTime,
                     execution.getTimeLimit());
            
            return ResponseEntity
                    .badRequest()
                    .body("Error timeLimit must be between "
                            + minExecutionTime + " Sec and " + maxExecutionTime + " Sec, provided : "
                            + execution.getTimeLimit());
        }
    
        builderDockerImage(execution);
    
        Result result = containerService.runCode(execution.getImageName(), execution.getExpectedOutputFile());
        
        if (deleteDockerImage) {
            try {
                containerService.deleteImage(execution.getImageName());
                log.info("Image " + execution.getImageName() + " has been deleted");
            } catch (Exception e) {
                log.warn("Error, can't delete image {} : {}", execution.getImageName(), e);
            }
        }
        
        String statusResponse = result.getVerdict();
        log.info(execution.getImageName() + " Status response is " + statusResponse);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(result.getOutput(), result.getExpectedOutput(), statusResponse, date));
    }
    
    private void builderDockerImage(Execution execution) throws CompilerServerException {
        try {
            execution.createExecutionDirectory();
        } catch (Exception e) {
            throw new CompilerServerException(execution.getImageName() + " " + e.getMessage());
        }
        
        try {
            log.info(execution.getImageName() + " Building the docker image");
            int status = containerService.buildImage(execution.getPath(), execution.getImageName());
            if (status == 0) {
                log.info(execution.getImageName() + " Docker image has been built");
            } else {
                throw new DockerBuildException(execution.getImageName() + " Error while building docker image");
            }
        } finally {
            try {
                execution.deleteExecutionDirectory();
                log.info(execution.getImageName() + " execution directory has been deleted");
            } catch (IOException e) {
                log.warn(execution.getImageName() + "Error while trying to delete execution directory, {}", e);
            }
        }
    }
}
