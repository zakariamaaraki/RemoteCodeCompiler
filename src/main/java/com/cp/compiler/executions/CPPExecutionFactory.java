package com.cp.compiler.executions;

import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Cpp execution factory.
 */
public class CPPExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    /**
     * Instantiates a new Cpp execution factory.
     *
     * @param executionCounter the execution counter for monitoring
     */
    public CPPExecutionFactory(Counter executionCounter) {
        this.executionCounter = executionCounter;
    }
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new CPPExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
    }
}
