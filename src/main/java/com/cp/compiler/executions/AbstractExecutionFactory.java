package com.cp.compiler.executions;

import com.cp.compiler.models.ConvertedTestCase;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The interface Abstract execution factory.
 */
@FunctionalInterface
public interface AbstractExecutionFactory {
    
    /**
     * Create execution.
     *
     * @param sourceCode  the source code
     * @param testCases   the test cases
     * @param timeLimit   the time limit
     * @param memoryLimit the memory limit
     * @return the execution
     */
    Execution createExecution(MultipartFile sourceCode,
                              List<ConvertedTestCase> testCases,
                              int timeLimit,
                              int memoryLimit);
}
