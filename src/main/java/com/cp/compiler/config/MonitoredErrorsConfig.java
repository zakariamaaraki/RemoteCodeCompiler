package com.cp.compiler.config;

import com.cp.compiler.exceptions.ErrorCode;
import com.cp.compiler.exceptions.ErrorCounterFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * The type Monitored errors config.
 */
@Configuration
public class MonitoredErrorsConfig {
    
    private MeterRegistry meterRegistry;
    
    /**
     * Instantiates a new Monitored errors config.
     *
     * @param meterRegistry the meter registry
     */
    public MonitoredErrorsConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        configure();
    }
    
    private void configure() {
        Arrays.stream(ErrorCode.values())
                .forEach(errorCode -> ErrorCounterFactory.registerCounter(errorCode,
                        meterRegistry.counter(errorCode.name().toLowerCase())));
    }
}
