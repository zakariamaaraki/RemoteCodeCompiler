package com.cp.compiler.repositories.executions;

import com.cp.compiler.executions.Execution;

import java.util.List;

public interface ExecutionRepository {

    List<Execution> getExecutions();

    Execution addExecution(Execution execution);

    Execution removeExecution(String executionId);
}
