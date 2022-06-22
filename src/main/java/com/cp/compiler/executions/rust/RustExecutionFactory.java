package com.cp.compiler.executions.rust;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Rust execution factory.
 */
public class RustExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    private final EntrypointFileGenerator entryPointFileGenerator;
    
    /**
     * Instantiates a new Rust execution factory.
     *
     * @param executionCounter        the execution counter for monitoring
     * @param entryPointFileGenerator the entry point file generator
     */
    public RustExecutionFactory(Counter executionCounter, EntrypointFileGenerator entryPointFileGenerator) {
        this.executionCounter = executionCounter;
        this.entryPointFileGenerator = entryPointFileGenerator;
    }
    
    /**
     * Create Rust execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     * @return the execution
     */
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new RustExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
}
