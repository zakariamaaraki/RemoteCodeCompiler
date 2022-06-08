package com.cp.compiler.executions.kotlin;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Java execution factory.
 */
public class KotlinExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    /**
     * Instantiates a new Java execution factory.
     *
     * @param executionCounter the execution counter for monitoring
     */
    public KotlinExecutionFactory(Counter executionCounter) {
        this.executionCounter = executionCounter;
    }
    
    
    /**
     * Create Kotlin execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @return the execution
     */
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new KotlinExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
    }
}
