package com.cp.compiler.api.controllers;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.repositories.executions.ExecutionRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExecutionController {

    private final ExecutionRepository executionRepository;

    public ExecutionController(ExecutionRepository executionRepository) {
        this.executionRepository = executionRepository;
    }

    @ApiOperation(value = "Return list of executions")
    @GetMapping("/executions")
    public List<Execution> getExecutions() {
        return executionRepository.getExecutions();
    }
}
