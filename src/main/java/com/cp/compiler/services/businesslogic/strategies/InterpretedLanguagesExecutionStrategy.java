package com.cp.compiler.services.businesslogic.strategies;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.CompilationResponse;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.services.platform.containers.ContainerService;
import com.cp.compiler.services.platform.resources.Resources;
import com.cp.compiler.consts.WellKnownMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The type Interpreted languages execution strategy.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Component("interpreted")
public class InterpretedLanguagesExecutionStrategy extends ExecutionStrategyDecorator {
    
    private final MeterRegistry meterRegistry;
    
    /**
     * Instantiates a new Interpreted languages execution strategy.
     *
     * @param containerService the container service
     * @param meterRegistry    the meter registry
     * @param resources        the resources
     */
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
    
    /**
     * Init.
     */
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
