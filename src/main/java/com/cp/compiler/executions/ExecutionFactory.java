package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Execution factory.
 */
public abstract class ExecutionFactory {
    
    private static Map<Language, AbstractExecutionFactory> registeredFactories = new EnumMap<>(Language.class);
    
    private ExecutionFactory() {}
    
    /**
     * Register.
     *
     * @param language the language
     * @param factory  the factory
     */
    public static void register(Language language, AbstractExecutionFactory factory) {
        registeredFactories.putIfAbsent(language, factory);
    }
    
    /**
     * Gets registered factories.
     *
     * @return the registered factories
     */
    public static Set<Language> getRegisteredFactories() {
        return registeredFactories
                .keySet()
                .stream()
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets execution.
     *
     * @param sourceCode  the source code
     * @param testCases   the test cases
     * @param timeLimit   the time limit
     * @param memoryLimit the memory limit
     * @param language    the language
     * @return the execution
     */
    public static Execution createExecution(MultipartFile sourceCode,
                                            List<ConvertedTestCase> testCases,
                                            int timeLimit,
                                            int memoryLimit,
                                            Language language) {
        AbstractExecutionFactory factory = registeredFactories.get(language);
        if (factory == null) {
            throw new FactoryNotFoundException("No ExecutionFactory registered for the language " + language);
        }
        
        return factory.createExecution(
                sourceCode,
                testCases,
                timeLimit,
                memoryLimit);
    }
}
