package com.cp.compiler.services.strategies;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.CompilationResponse;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.services.containers.ContainerService;
import com.cp.compiler.services.resources.Resources;
import com.cp.compiler.wellknownconstants.WellKnownMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component("interpreted")
public class InterpretedLanguagesExecutionStrategy extends ExecutionStrategy {
    
    private final MeterRegistry meterRegistry;
    
    public InterpretedLanguagesExecutionStrategy(ContainerService containerService,
                                                 MeterRegistry meterRegistry,
                                                 Resources resources) {
        super(containerService, meterRegistry, resources);
        this.meterRegistry = meterRegistry;
    }
    
    @Override
    public CompilationResponse compile(Execution execution) {
        log.info("Compilation step is skipped for interpreted languages");
        return buildCompilationResponseForInterpretedLanguages();
    }
    
    @PostConstruct
    public void init() {
        executionTimer = meterRegistry.timer(WellKnownMetrics.EXECUTION_TIMER, "compiler", "execution");
    }
    
    private CompilationResponse buildCompilationResponseForInterpretedLanguages() {
        return CompilationResponse
                .builder()
                .compilationDuration(0)
                .error("")
                .verdict(Verdict.ACCEPTED)
                .build();
    }
}
