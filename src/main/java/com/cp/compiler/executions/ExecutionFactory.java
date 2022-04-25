package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.models.Language;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The type Execution factory.
 */
public class ExecutionFactory {
    
    private static Map<Language, Supplier<? extends AbstractExecutionFactory>> registeredSuppliers
            = new EnumMap<>(Language.class);
    
    private ExecutionFactory() {}
    
    public static void register(Language language, Supplier<? extends AbstractExecutionFactory> supplier) {
        registeredSuppliers.putIfAbsent(language, supplier);
    }
    
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
    public static Execution createExecution(MultipartFile sourceCode,
                                            MultipartFile inputFile,
                                            MultipartFile expectedOutputFile,
                                            int timeLimit,
                                            int memoryLimit,
                                            Language language) {
        Supplier<? extends AbstractExecutionFactory> supplier = registeredSuppliers.get(language);
        if (supplier == null) {
            throw new FactoryNotFoundException("No ExecutionFactory registered for the language " + language);
        }
        return supplier.get().createExecution(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
    }
}
