package com.cp.compiler.executions.python;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Python execution factory.
 */
public class PythonExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    /**
     * Instantiates a new Python execution factory.
     *
     * @param executionCounter the execution counter for monitoring
     */
    public PythonExecutionFactory(Counter executionCounter) {
        this.executionCounter = executionCounter;
    }
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new PythonExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
    }
}
