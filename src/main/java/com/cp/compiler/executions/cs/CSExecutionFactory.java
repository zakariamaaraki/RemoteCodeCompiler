package com.cp.compiler.executions.cs;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Cs execution factory.
 */
public class CSExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    /**
     * Instantiates a new C# execution factory.
     *
     * @param executionCounter the execution counter
     */
    public CSExecutionFactory(Counter executionCounter) {
        this.executionCounter = executionCounter;
    }
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new CSExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
    }
}
