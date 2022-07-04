package com.cp.compiler.executions;

import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Language execution factory.
 */
public class LanguageExecutionFactory implements AbstractLanguageExecutionFactory {
    
    private final FunctionalExecutionFactory functionalExecutionFactory;
    
    /**
     * Instantiates a new Language execution factory.
     *
     * @param functionalExecutionFactory the functional execution factory
     */
    public LanguageExecutionFactory(FunctionalExecutionFactory functionalExecutionFactory) {
        this.functionalExecutionFactory = functionalExecutionFactory;
    }
    
    /**
     * Create execution execution.
     *
     * @param sourceCode              the source code
     * @param inputFile               the input file
     * @param expectedOutputFile      the expected output file
     * @param timeLimit               the time limit
     * @param memoryLimit             the memory limit
     * @return the execution
     */
    @Override
    public Execution createExecution(MultipartFile sourceCode,
                                     MultipartFile inputFile,
                                     MultipartFile expectedOutputFile,
                                     int timeLimit,
                                     int memoryLimit) {
        return functionalExecutionFactory.create(
                sourceCode,
                inputFile,
                expectedOutputFile,
                timeLimit,
                memoryLimit);
    }
}
