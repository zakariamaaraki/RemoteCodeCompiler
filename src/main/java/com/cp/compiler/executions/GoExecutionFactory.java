package com.cp.compiler.executions;

import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Go execution factory.
 */
public class GoExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    /**
     * Instantiates a new Go execution factory.
     *
     * @param executionCounter the execution counter for monitoring
     */
    public GoExecutionFactory(Counter executionCounter) {
        this.executionCounter = executionCounter;
    }
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new GoExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
    }
}
