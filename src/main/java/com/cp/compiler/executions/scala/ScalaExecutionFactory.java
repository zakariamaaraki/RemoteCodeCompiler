package com.cp.compiler.executions.scala;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Java execution factory.
 */
public class ScalaExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    private final EntrypointFileGenerator entryPointFileGenerator;
    
    /**
     * Instantiates a new Java execution factory.
     *
     * @param executionCounter        the execution counter for monitoring
     * @param entryPointFileGenerator the entry point file generator
     */
    public ScalaExecutionFactory(Counter executionCounter, EntrypointFileGenerator entryPointFileGenerator) {
        this.executionCounter = executionCounter;
        this.entryPointFileGenerator = entryPointFileGenerator;
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
        return new ScalaExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
}
