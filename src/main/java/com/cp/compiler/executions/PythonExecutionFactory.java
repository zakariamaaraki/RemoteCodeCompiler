package com.cp.compiler.executions;

import org.springframework.web.multipart.MultipartFile;

/**
 * The type Python execution factory.
 */
public class PythonExecutionFactory implements AbstractExecutionFactory {
    
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return new PythonExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
    }
}
