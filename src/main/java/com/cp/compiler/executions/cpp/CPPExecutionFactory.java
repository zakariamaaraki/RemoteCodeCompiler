package com.cp.compiler.executions.cpp;

import com.cp.compiler.executions.AbstractExecutionFactory;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Cpp execution factory.
 */
public class CPPExecutionFactory implements AbstractExecutionFactory {
    
    private final Counter executionCounter;
    
    private final EntrypointFileGenerator entryPointFileGenerator;
    
    /**
     * Instantiates a new Cpp execution factory.
     *
     * @param executionCounter        the execution counter for monitoring
     * @param entryPointFileGenerator the entry point file generator
     */
    public CPPExecutionFactory(Counter executionCounter, EntrypointFileGenerator entryPointFileGenerator) {
        this.executionCounter = executionCounter;
        this.entryPointFileGenerator = entryPointFileGenerator;
    }
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new CPPExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
}
