package com.cp.compiler.services.businesslogic.strategies;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.ExecutionResponse;
import com.cp.compiler.services.platform.containers.ContainerService;
import com.cp.compiler.services.platform.resources.Resources;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExecutionStrategyDecorator extends ExecutionStrategy {

    /**
     * Instantiates a new Execution strategy.
     *
     * @param containerService the container service
     * @param meterRegistry    the meter registry
     * @param resources        the resources
     */
    protected ExecutionStrategyDecorator(ContainerService containerService, MeterRegistry meterRegistry, Resources resources) {
        super(containerService, meterRegistry, resources);
    }

    @Override
    public ExecutionResponse run(Execution execution, boolean deleteImageAfterExecution) {

        long startTime = System.nanoTime();

        var executionResponse = super.run(execution, deleteImageAfterExecution);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        // Convert elapsed time to seconds
        double elapsedTimeInMilliSeconds = (double) elapsedTime / 1_000_000;
        log.info("Total execution duration took {} milliseconds", elapsedTimeInMilliSeconds);

        // Update last execution time, used to suggest retry in case of a throttling.
        resources.lastExecutionDuration.set(elapsedTimeInMilliSeconds);

        return executionResponse;
    }
}
