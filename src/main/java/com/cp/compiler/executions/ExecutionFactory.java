package com.cp.compiler.executions;

import com.cp.compiler.exceptions.FactoryNotFoundException;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.Language;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Execution factory.
 * This class contains all factories needed to create an Execution class.
 *
 * @author Zakaria Maaraki
 */
public abstract class ExecutionFactory {
    
    private static Map<Language, ExecutionType> registeredExecutionTypes = new EnumMap<>(Language.class);
    
    private static Map<Language, AbstractExecutionFactory> registeredFactories = new EnumMap<>(Language.class);
    
    private ExecutionFactory() {}
    
    /**
     * Register.
     *
     * @param language the language
     * @param factory  the factory
     */
    public static void registerExecution(Language language, AbstractExecutionFactory factory) {
        registeredFactories.putIfAbsent(language, factory);
    }
    
    /**
     * Register execution type.
     *
     * @param language      the language
     * @param executionType the execution type
     */
    public static void registerExecutionType(Language language, ExecutionType executionType) {
        registeredExecutionTypes.putIfAbsent(language, executionType);
    }
    
    /**
     * Gets execution type.
     *
     * @param language the language
     * @return the execution type
     */
    public static ExecutionType getExecutionType(Language language) {
        return registeredExecutionTypes.get(language);
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
                                            List<TransformedTestCase> testCases,
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
