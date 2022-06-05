package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.models.Language;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The type Execution factory.
 */
public abstract class ExecutionFactory {
    
    private static Map<Language, Supplier<? extends AbstractExecutionFactory>> registeredSuppliers
            = new EnumMap<>(Language.class);
    
    private ExecutionFactory() {}
    
    /**
     * Register.
     *
     * @param language the language
     * @param supplier the supplier
     */
    public static void register(Language language, Supplier<? extends AbstractExecutionFactory> supplier) {
        registeredSuppliers.putIfAbsent(language, supplier);
    }
    
    /**
     * Gets registered factories.
     *
     * @return the registered factories
     */
    public static Set<Language> getRegisteredFactories() {
        return registeredSuppliers
                .keySet()
                .stream()
                .collect(Collectors.toSet());
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
        
        return supplier.get().createExecution(
                sourceCode,
                inputFile,
                expectedOutputFile,
                timeLimit,
                memoryLimit);
    }
}
