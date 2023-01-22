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
import com.cp.compiler.models.Language;
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
    
    /**
     * Instantiates a new Configure languages.
     *
     * @param meterRegistry           the meter registry for monitoring
     * @param entryPointFileGenerator the entry point file generator
     */
    public LanguagesConfig(MeterRegistry meterRegistry, EntrypointFileGenerator entryPointFileGenerator) {
        this.entrypointFileGenerator = entryPointFileGenerator;
        this.meterRegistry = meterRegistry;
        configureExecutionTypes(); // Note: configure execution types should come before configure languages!
        configureLanguages();
    }
    
    private void configureExecutionTypes() {
        for (Language language : Language.values()) {
            Counter executionsCounter = meterRegistry.counter(Language.JAVA.getExecutionCounter());
            ExecutionFactory.registerExecutionType(language, new ExecutionType(executionsCounter, entrypointFileGenerator));
        }
    }
    
    private void configureLanguages() {
        // Register factories
        register(Language.JAVA,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.JAVA)));
    
        register(Language.PYTHON,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new PythonExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.PYTHON)));
    
        register(Language.C,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new CExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.C)));
    
        register(Language.CPP,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new CPPExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.CPP)));
    
        register(Language.GO,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new GoExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.GO)));
    
        register(Language.CS,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new CSExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.CS)));
    
        register(Language.KOTLIN,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new KotlinExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.KOTLIN)));
    
        register(Language.SCALA,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new ScalaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.SCALA)));
    
        register(Language.RUST,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new RustExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.RUST)));
    
        register(Language.RUBY,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new RubyExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.RUBY)));
    
        register(Language.HASKELL,
                (sourceCode, testCases, timeLimit, memoryLimit) -> new HaskellExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit,
                        ExecutionFactory.getExecutionType(Language.HASKELL)));
    }
    
    private void register(Language language, AbstractExecutionFactory executionFactory) {
        ExecutionFactory.registerExecution(language, executionFactory );
    }
}
