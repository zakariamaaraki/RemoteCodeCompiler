package com.cp.compiler.repositories.executions;

import com.cp.compiler.executions.Execution;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ExecutionRepositoryDefault implements ExecutionRepository {

    private HashMap<String, Execution> executions = new HashMap<>();

    /**
     * @return List<Execution> list of executions
     */
    @Override
    public List<Execution> getExecutions() {
        return executions
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * @return Execution the added execution
     */
    @Override
    public Execution addExecution(Execution execution) {
        executions.put(execution.getId(), execution);
        return execution;
    }

    /**
     * @return Execution the removed execution
     */
    @Override
    public Execution removeExecution(String executionId) {
        return executions.remove(executionId);
    }
}
