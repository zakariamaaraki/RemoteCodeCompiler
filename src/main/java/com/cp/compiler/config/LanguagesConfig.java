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
        configure();
    }
    
    private void configure() {
        // Register factories
        register(Language.JAVA,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.JAVA.getExecutionCounter());
                    return new JavaExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.PYTHON,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.PYTHON.getExecutionCounter());
                    return new PythonExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.C,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.C.getExecutionCounter());
                    return new CExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.CPP,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.CPP.getExecutionCounter());
                    return new CPPExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.GO,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.GO.getExecutionCounter());
                    return new GoExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.CS,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.CS.getExecutionCounter());
                    return new CSExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.KOTLIN,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.KOTLIN.getExecutionCounter());
                    return new KotlinExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.SCALA,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.SCALA.getExecutionCounter());
                    return new ScalaExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.RUST,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.RUST.getExecutionCounter());
                    return new RustExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.RUBY,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.RUBY.getExecutionCounter());
                    return new RubyExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.HASKELL,
                (sourceCode, testCases, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(Language.HASKELL.getExecutionCounter());
                    return new HaskellExecution(
                            sourceCode,
                            testCases,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    }
    
    private void register(Language language, AbstractExecutionFactory executionFactory) {
        ExecutionFactory.register(language, executionFactory );
    }
}
