package com.cp.compiler.config;

import com.cp.compiler.executions.*;
import com.cp.compiler.models.Language;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

/**
 * The type Configure languages.
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
        ExecutionFactory.register(Language.JAVA, () -> {
            return new JavaExecutionFactory(meterRegistry.counter("java.counter"));
        });
        ExecutionFactory.register(Language.PYTHON, () -> {
            return new PythonExecutionFactory(meterRegistry.counter("python.counter"));
        });
        ExecutionFactory.register(Language.C, () -> {
            return new CExecutionFactory(meterRegistry.counter("c.counter"));
        });
        ExecutionFactory.register(Language.CPP, () -> {
            return new CPPExecutionFactory(meterRegistry.counter("cpp.counter"));
        });
    }
}
