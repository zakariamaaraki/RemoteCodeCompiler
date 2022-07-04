package com.cp.compiler.config;

import com.cp.compiler.executions.*;
import com.cp.compiler.executions.CExecution;
import com.cp.compiler.executions.CPPExecution;
import com.cp.compiler.executions.CSExecution;
import com.cp.compiler.executions.GoExecution;
import com.cp.compiler.executions.HaskellExecution;
import com.cp.compiler.executions.JavaExecution;
import com.cp.compiler.executions.KotlinExecution;
import com.cp.compiler.executions.PythonExecution;
import com.cp.compiler.executions.RubyExecution;
import com.cp.compiler.executions.RustExecution;
import com.cp.compiler.executions.ScalaExecution;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.WellKnownMetrics;
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
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.JAVA_COUNTER_NAME);
                    return new JavaExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.PYTHON,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.PYTHON_COUNTER_NAME);
                    return new PythonExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.C,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.C_COUNTER_NAME);
                    return new CExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.CPP,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.CPP_COUNTER_NAME);
                    return new CPPExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.GO,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.GO_COUNTER_NAME);
                    return new GoExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.CS,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.CS_COUNTER_NAME);
                    return new CSExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.KOTLIN,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.KOTLIN_COUNTER_NAME);
                    return new KotlinExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.SCALA,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.SCALA_COUNTER_NAME);
                    return new ScalaExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.RUST,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.RUST_COUNTER_NAME);
                    return new RustExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.RUBY,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.RUBY_COUNTER_NAME);
                    return new RubyExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
    
        register(Language.HASKELL,
                (sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit) -> {
                    Counter executionsCounter = meterRegistry.counter(WellKnownMetrics.HASKELL_COUNTER_NAME);
                    return new HaskellExecution(
                            sourceCode,
                            inputFile,
                            expectedOutputFile,
                            timeLimit,
                            memoryLimit,
                            executionsCounter,
                            entrypointFileGenerator);
                });
}
    
    private void register(Language language,
                          FunctionalExecutionFactory functionalExecutionFactory) {
        LanguageExecutionFactory languageExecutionFactory = new LanguageExecutionFactory(functionalExecutionFactory);
        ExecutionFactory.register(language, () -> languageExecutionFactory);
    }
}
