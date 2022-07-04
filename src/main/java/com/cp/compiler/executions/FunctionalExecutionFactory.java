package com.cp.compiler.executions;

import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Functional execution factory.
 */
@FunctionalInterface
public interface FunctionalExecutionFactory {
    
    /**
     * Create execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @return the execution
     */
    Execution create(MultipartFile sourceCode,
                     MultipartFile inputFile,
                     MultipartFile expectedOutputFile,
                     int timeLimit,
                     int memoryLimit);
}
