package com.cp.compiler.services.platform.garbagecollector;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.repositories.executions.ExecutionRepository;
import com.cp.compiler.services.platform.containers.ContainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ExecutionGarbageCollectorDefault implements ExecutionGarbageCollector{

    private ExecutionRepository executionRepository;

    private ContainerService containerService;

    public ExecutionGarbageCollectorDefault(ExecutionRepository executionRepository,
                                            ContainerService containerService) {
        this.executionRepository = executionRepository;
        this.containerService = containerService;
    }

    @Scheduled(fixedRate = 600000) // 10 minutes = 600,000 milliseconds
    @Override
    public void collectExecutions() {
        log.info("Garbage collector's execution started");

        int counter = 0;

        for (Execution execution : executionRepository.getExecutions()) {
            if (shouldBeDeleted(execution)) {
                log.info("The execution id = {} with an execution state set to = {} will be collected by the garbage collector",
                        execution.getId(),
                        execution.getExecutionState());

                for (TransformedTestCase testCase : execution.getTestCases()) {
                    containerService.deleteContainer(execution.getTestCaseContainerName(testCase.getTestCaseId()));
                }

                counter++;
                executionRepository.removeExecution(execution.getId());
            }
        }

        log.info("Garbage collector's execution ended, {} executions were removed", counter);
    }

    private boolean shouldBeDeleted(Execution execution) {
        return execution.getDateTime().plusMinutes(10).compareTo(LocalDateTime.now()) <= 0;
    }
}
