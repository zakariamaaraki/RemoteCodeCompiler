package com.cp.compiler.services.businesslogic;

import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.exceptions.*;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionState;
import com.cp.compiler.models.*;
import com.cp.compiler.services.businesslogic.strategies.ExecutionStrategy;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Compiler Service Class, this class provides compilation utilities for several programing languages
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Service("client")
public class CompilerServiceDefault implements CompilerService {
    
    private final ExecutionStrategy compiledLanguagesExecutionStrategy;
    
    private final ExecutionStrategy interpretedLanguagesExecutionStrategy;

    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;
    
    private final ExecutorService threadPool;
    
    /**
     * Instantiates a new Compiler service.
     *
     * @param compiledLanguagesExecutionStrategy    the compiled languages execution strategy
     * @param interpretedLanguagesExecutionStrategy the interpreted languages execution strategy
     */
    public CompilerServiceDefault(@Qualifier("compiled") ExecutionStrategy compiledLanguagesExecutionStrategy,
                                  @Qualifier("interpreted") ExecutionStrategy interpretedLanguagesExecutionStrategy) {
        this.compiledLanguagesExecutionStrategy = compiledLanguagesExecutionStrategy;
        this.interpretedLanguagesExecutionStrategy = interpretedLanguagesExecutionStrategy;
        this.threadPool = Executors.newCachedThreadPool();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<RemoteCodeCompilerResponse> execute(Execution execution) {
        
        LocalDateTime dateTime = LocalDateTime.now();
        
        // Build execution environment (create directory, upload files, ...)
        buildExecutionEnvironment(execution);
        
        ExecutionStrategy executionStrategy;
        
        try {
            CompilationResponse compilationResponse;
            
            // Choose which strategy to apply
            executionStrategy = getExecutionStrategy(execution.getLanguage().isCompiled());

            compilationResponse = executionStrategy.compile(execution);
    
            if (compilationResponse.getVerdict().equals(Verdict.COMPILATION_ERROR)) {

                execution.setExecutionState(ExecutionState.Error);

                log.warn("Potential error occurred during compilation of execution id = {}, error = {}",
                        execution.getId(),
                        compilationResponse.getError());
                
                var response = new RemoteCodeCompilerExecutionResponse(
                        compilationResponse.getVerdict().getStatusResponse(),
                        compilationResponse.getVerdict().getStatusCode(),
                        compilationResponse.getError(),
                        new LinkedHashMap<>(),
                        compilationResponse.getCompilationDuration(),
                        execution.getTimeLimit(),
                        execution.getMemoryLimit(),
                        execution.getLanguage(),
                        dateTime);
                return ResponseEntity.ok(new RemoteCodeCompilerResponse(response));
            }

            execution.setExecutionState(ExecutionState.BinariesReady);
            
            ExecutionResponse executionResponse = executionStrategy.run(execution, deleteDockerImage);
    
            log.info("Execution finished, the verdict is {}", executionResponse.getVerdict().getStatusResponse());
            
            var response = new RemoteCodeCompilerExecutionResponse(
                    executionResponse.getVerdict().getStatusResponse(),
                    executionResponse.getVerdict().getStatusCode(),
                    executionResponse.getError(),
                    executionResponse.getTestCasesResult(),
                    compilationResponse.getCompilationDuration(),
                    execution.getTimeLimit(),
                    execution.getMemoryLimit(),
                    execution.getLanguage(),
                    dateTime);
    
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new RemoteCodeCompilerResponse(response));
        } finally {
            // Clean up asynchronously
            threadPool.execute(() -> deleteExecutionEnvironment(execution));
        }
    }
    
    private ExecutionStrategy getExecutionStrategy(boolean isCompiledLanguage) {
        if (isCompiledLanguage) {
            return compiledLanguagesExecutionStrategy;
        }
        return interpretedLanguagesExecutionStrategy;
    }
    
    private void buildExecutionEnvironment(Execution execution) {
        try {
            log.info("Creating execution directory: {}", execution.getExecutionFolderName());
            execution.createExecutionDirectory();
        } catch (Throwable e) {
            log.error("Error while building execution environment", e);
            throw new CompilerServerInternalException(e.getMessage());
        }
    }
    
    private void deleteExecutionEnvironment(Execution execution) {
        try {
            execution.deleteExecutionDirectory();
            log.info("Execution directory {} has been deleted", execution.getExecutionFolderName());
        } catch (IOException e) {
            log.warn("Error while trying to delete execution directory", e);
        }
    }
}
