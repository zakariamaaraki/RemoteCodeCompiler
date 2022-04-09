package com.cp.compiler.executions;

import org.springframework.web.multipart.MultipartFile;

/**
 * The type Java execution factory.
 */
public class JavaExecutionFactory implements AbstractExecutionFactory {
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new JavaExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
    }
}
