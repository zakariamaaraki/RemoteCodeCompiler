package com.cp.compiler.executions.python;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Python execution factory.
 */
public class PythonExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    private final EntrypointFileGenerator entryPointFileGenerator;
    
    /**
     * Instantiates a new Python execution factory.
     *
     * @param executionCounter        the execution counter for monitoring
     * @param entryPointFileGenerator the entry point file generator
     */
    public PythonExecutionFactory(Counter executionCounter, EntrypointFileGenerator entryPointFileGenerator) {
        this.executionCounter = executionCounter;
        this.entryPointFileGenerator = entryPointFileGenerator;
    }
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new PythonExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
}
