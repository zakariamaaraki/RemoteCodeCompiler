package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Execution factory.
 */
public class ExecutionFactory {
    
    private ExecutionFactory() {}
    
    /**
     * Gets execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param language           the language
     * @return the execution
     */
    public static Execution getExecution(MultipartFile sourceCode,
                                         MultipartFile inputFile,
                                         MultipartFile expectedOutputFile,
                                         int timeLimit,
                                         int memoryLimit,
                                         Language language) {
        if (language == Language.JAVA) {
            return new JavaExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
        } else if (language == Language.PYTHON) {
            return new PythonExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
        } else if (language == Language.C) {
            return new CExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
        } else {
            return new CPPExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
        }
    }
}
