package com.cp.compiler.config;

import com.cp.compiler.executions.*;
import com.cp.compiler.executions.languages.CExecution;
import com.cp.compiler.executions.languages.CPPExecution;
import com.cp.compiler.executions.languages.CSExecution;
import com.cp.compiler.executions.languages.GoExecution;
import com.cp.compiler.executions.languages.HaskellExecution;
import com.cp.compiler.executions.languages.JavaExecution;
import com.cp.compiler.executions.languages.KotlinExecution;
import com.cp.compiler.executions.languages.PythonExecution;
import com.cp.compiler.executions.languages.RubyExecution;
import com.cp.compiler.executions.languages.RustExecution;
import com.cp.compiler.executions.languages.ScalaExecution;
import com.cp.compiler.contract.Language;
import com.cp.compiler.repositories.executions.ExecutionRepository;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

/**
 * The type Configure languages.
 * Any new language added should be registered in the factory
 */
@Configuration
public class LanguagesConfig {
    
    private EntrypointFileGenerator entrypointFileGenerator;
    
    private MeterRegistry meterRegistry;

    private ExecutionRepository executionRepository;
    
    /**
     * Instantiates a new Configure languages.
     *
     * @param meterRegistry           the meter registry for monitoring
     * @param entryPointFileGenerator the entry point file generator
     * @param executionRepository the execution repository
     */
    public LanguagesConfig(MeterRegistry meterRegistry,
                           EntrypointFileGenerator entryPointFileGenerator,
                           ExecutionRepository executionRepository) {
        this.entrypointFileGenerator = entryPointFileGenerator;
        this.meterRegistry = meterRegistry;
        this.executionRepository = executionRepository;
        configureExecutionTypes();
        configureLanguages();
    }
    
    private void configureExecutionTypes() {
        for (Language language : Language.values()) {
            Counter executionsCounter = meterRegistry.counter(language.getExecutionCounter());
            ExecutionFactory.registerExecutionType(language, new ExecutionType(executionsCounter, entrypointFileGenerator, executionRepository));
        }
    }
    
    private void configureLanguages() {
        // Register factories
        register(Language.JAVA,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.PYTHON,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new PythonExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.C,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new CExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.CPP,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new CPPExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.GO,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new GoExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.CS,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new CSExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.KOTLIN,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new KotlinExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.SCALA,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new ScalaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.RUST,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new RustExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.RUBY,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new RubyExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        register(Language.HASKELL,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new HaskellExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    }
    
    private void register(Language language, AbstractExecutionFactory executionFactory) {
        ExecutionFactory.registerExecution(language, executionFactory );
    }
}
