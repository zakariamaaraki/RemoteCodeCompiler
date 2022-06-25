package com.cp.compiler.config;

import com.cp.compiler.executions.*;
import com.cp.compiler.executions.c.CExecutionFactory;
import com.cp.compiler.executions.cpp.CPPExecutionFactory;
import com.cp.compiler.executions.cs.CSExecutionFactory;
import com.cp.compiler.executions.go.GoExecutionFactory;
import com.cp.compiler.executions.haskell.HaskellExecutionFactory;
import com.cp.compiler.executions.java.JavaExecutionFactory;
import com.cp.compiler.executions.kotlin.KotlinExecutionFactory;
import com.cp.compiler.executions.python.PythonExecutionFactory;
import com.cp.compiler.executions.ruby.RubyExecutionFactory;
import com.cp.compiler.executions.rust.RustExecutionFactory;
import com.cp.compiler.executions.scala.ScalaExecutionFactory;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.WellKnownMetrics;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

/**
 * The type Configure languages.
 * Any new language added should be registered in the factory
 */
@Configuration
public class LanguagesConfig {
    
    /**
     * Instantiates a new Configure languages.
     *
     * @param meterRegistry           the meter registry for monitoring
     * @param entryPointFileGenerator the entry point file generator
     */
    public LanguagesConfig(MeterRegistry meterRegistry, EntrypointFileGenerator entryPointFileGenerator) {
        configure(meterRegistry, entryPointFileGenerator);
    }
    
    private void configure(MeterRegistry meterRegistry, EntrypointFileGenerator entryPointFileGenerator) {
        // register factories
        register(Language.JAVA,  new JavaExecutionFactory(meterRegistry.counter(WellKnownMetrics.JAVA_COUNTER_NAME), entryPointFileGenerator));
        register(Language.PYTHON, new PythonExecutionFactory(meterRegistry.counter(WellKnownMetrics.PYTHON_COUNTER_NAME), entryPointFileGenerator));
        register(Language.C, new CExecutionFactory(meterRegistry.counter(WellKnownMetrics.C_COUNTER_NAME), entryPointFileGenerator));
        register(Language.CPP, new CPPExecutionFactory(meterRegistry.counter(WellKnownMetrics.CPP_COUNTER_NAME), entryPointFileGenerator));
        register(Language.GO, new GoExecutionFactory(meterRegistry.counter(WellKnownMetrics.GO_COUNTER_NAME), entryPointFileGenerator));
        register(Language.CS, new CSExecutionFactory(meterRegistry.counter(WellKnownMetrics.CS_COUNTER_NAME), entryPointFileGenerator));
        register(Language.KOTLIN, new KotlinExecutionFactory(meterRegistry.counter(WellKnownMetrics.KOTLIN_COUNTER_NAME), entryPointFileGenerator));
        register(Language.SCALA, new ScalaExecutionFactory(meterRegistry.counter(WellKnownMetrics.SCALA_COUNTER_NAME), entryPointFileGenerator));
        register(Language.RUST, new RustExecutionFactory(meterRegistry.counter(WellKnownMetrics.RUST_COUNTER_NAME), entryPointFileGenerator));
        register(Language.RUBY, new RubyExecutionFactory(meterRegistry.counter(WellKnownMetrics.RUBY_COUNTER_NAME), entryPointFileGenerator));
        register(Language.HASKELL, new HaskellExecutionFactory(meterRegistry.counter(WellKnownMetrics.HASKELL_COUNTER_NAME), entryPointFileGenerator));
    }
    
    private void register(Language language, AbstractExecutionFactory executionFactory) {
        ExecutionFactory.register(language, () -> executionFactory);
    }
}
