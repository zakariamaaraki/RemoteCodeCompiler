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
        register(Language.JAVA,  new JavaExecutionFactory(meterRegistry.counter("java.counter")));
        register(Language.PYTHON, new PythonExecutionFactory(meterRegistry.counter("python.counter")));
        register(Language.C, new CExecutionFactory(meterRegistry.counter("c.counter")));
        register(Language.CPP, new CPPExecutionFactory(meterRegistry.counter("cpp.counter")));
    }
    
    private void register(Language language, AbstractExecutionFactory executionFactory) {
        ExecutionFactory.register(language, () -> executionFactory);
    }
}
