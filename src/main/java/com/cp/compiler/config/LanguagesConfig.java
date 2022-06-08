package com.cp.compiler.config;

import com.cp.compiler.executions.*;
import com.cp.compiler.executions.c.CExecutionFactory;
import com.cp.compiler.executions.cpp.CPPExecutionFactory;
import com.cp.compiler.executions.cs.CSExecutionFactory;
import com.cp.compiler.executions.go.GoExecutionFactory;
import com.cp.compiler.executions.java.JavaExecutionFactory;
import com.cp.compiler.executions.kotlin.KotlinExecutionFactory;
import com.cp.compiler.executions.python.PythonExecutionFactory;
import com.cp.compiler.models.Language;
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
     * @param meterRegistry the meter registry for monitoring
     */
    public LanguagesConfig(MeterRegistry meterRegistry) {
        configure(meterRegistry);
    }
    
    private void configure(MeterRegistry meterRegistry) {
        register(Language.JAVA,  new JavaExecutionFactory(meterRegistry.counter("java.counter")));
        register(Language.PYTHON, new PythonExecutionFactory(meterRegistry.counter("python.counter")));
        register(Language.C, new CExecutionFactory(meterRegistry.counter("c.counter")));
        register(Language.CPP, new CPPExecutionFactory(meterRegistry.counter("cpp.counter")));
        register(Language.GO, new GoExecutionFactory(meterRegistry.counter("go.counter")));
        register(Language.CS, new CSExecutionFactory(meterRegistry.counter("cs.counter")));
        register(Language.KOTLIN, new KotlinExecutionFactory(meterRegistry.counter("kotlin.counter")));
    }
    
    private void register(Language language, AbstractExecutionFactory executionFactory) {
        ExecutionFactory.register(language, () -> executionFactory);
    }
}
