package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Request;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Compiler proxy Service.
 */
@Slf4j
@Service("proxy")
public class CompilerProxy implements CompilerService {
    
    @Autowired
    @Qualifier("client")
    private CompilerService compilerService;
    
    private AtomicLong executionsCounter = new AtomicLong(0);
    
    @Getter
    @Value("${compiler.max-requests}")
    private long maxRequests;
    
    @Override
    public int getMaxExecutionMemory() {
        return compilerService.getMaxExecutionMemory();
    }
    
    @Override
    public int getMinExecutionMemory() {
        return compilerService.getMinExecutionMemory();
    }
    
    @Override
    public int getMaxExecutionTime() {
        return compilerService.getMaxExecutionTime();
    }
    
    @Override
    public int getMinExecutionTime() {
        return compilerService.getMinExecutionTime();
    }
    
    @Override
    public boolean isDeleteDockerImage() {
        return compilerService.isDeleteDockerImage();
    }
    
    @Override
    public ResponseEntity<Object> compile(Request request) throws Exception {
        Execution execution = ExecutionFactory.createExecution(request.getExpectedOutput(),
                                                               request.getSourceCode(),
                                                               request.getInput(),
                                                               request.getTimeLimit(),
                                                               request.getMemoryLimit(),
                                                               request.getLanguage());
        return compile(execution);
    }
    
    @Override
    public ResponseEntity<Object> compile(Execution execution) throws Exception {
        Optional<ResponseEntity> requestValidationError = validateRequest(execution);
        if (requestValidationError.isPresent()) {
            // the request is not valid
            return requestValidationError.get();
        }
        if (allow()) {
            long counter = executionsCounter.incrementAndGet();
            log.info("New request: {}, total: {}", execution.getImageName(), counter);
            var response = compilerService.compile(execution);
            executionsCounter.decrementAndGet();
            return response;
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Request throttled, service reached max allowed requests");
    }
    
    private Optional<ResponseEntity> validateRequest(Execution execution) {
        if (execution.getSourceCodeFile() == null) {
            return Optional.of(buildOutputError(execution, "Bad request, source code cannot be null"));
        }
    
        if (execution.getExpectedOutputFile() == null) {
            return Optional.of(buildOutputError(execution, "Bad request, expected output cannot be null"));
        }
        
        if (execution.getTimeLimit() < getMinExecutionTime() || execution.getTimeLimit() > getMaxExecutionTime()) {
            String errorMessage = "Bad request, time limit must be between "
                    + getMinExecutionTime() + " Sec and " + getMaxExecutionTime() + " Sec, provided : "
                    + execution.getTimeLimit();
    
            return Optional.of(buildOutputError(execution, errorMessage));
        }
    
        if (execution.getMemoryLimit() < getMinExecutionMemory() || execution.getMemoryLimit() > getMaxExecutionMemory()) {
            String errorMessage = "Bad request, memory limit must be between "
                    + getMinExecutionMemory() + " MB and " + getMaxExecutionMemory() + " MB, provided : "
                    + execution.getMemoryLimit();
    
            return Optional.of(buildOutputError(execution, errorMessage));
        }
        return Optional.ofNullable(null);
    }
    
    private ResponseEntity buildOutputError(Execution execution, String errorMessage) {
        log.info(execution.getImageName() + " " + errorMessage);
    
        return ResponseEntity.badRequest()
                             .body(errorMessage);
    }
    
    private boolean allow() {
        return executionsCounter.get() <= maxRequests;
    }
}
