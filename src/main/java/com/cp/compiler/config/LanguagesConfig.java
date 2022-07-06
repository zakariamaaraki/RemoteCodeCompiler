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
                    Counter executionsCounter = meterRegistry.counter(Language.JAVA.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.PYTHON.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.C.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.CPP.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.GO.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.CS.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.KOTLIN.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.SCALA.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.RUST.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.RUBY.getExecutionCounter());
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
                    Counter executionsCounter = meterRegistry.counter(Language.HASKELL.getExecutionCounter());
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
    
    private void register(Language language, AbstractExecutionFactory executionFactory) {
        ExecutionFactory.register(language, executionFactory );
    }
}
