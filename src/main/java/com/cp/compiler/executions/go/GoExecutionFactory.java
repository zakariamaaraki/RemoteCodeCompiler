package com.cp.compiler.executions.go;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Go execution factory.
 */
public class GoExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    private final EntrypointFileGenerator entryPointFileGenerator;
    
    /**
     * Instantiates a new Go execution factory.
     *
     * @param executionCounter        the execution counter for monitoring
     * @param entryPointFileGenerator the entry point file generator
     */
    public GoExecutionFactory(Counter executionCounter, EntrypointFileGenerator entryPointFileGenerator) {
        this.executionCounter = executionCounter;
        this.entryPointFileGenerator = entryPointFileGenerator;
    }
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new GoExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
}
