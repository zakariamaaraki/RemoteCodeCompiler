package com.cp.compiler.api.controllers;

import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.contract.problems.ProblemExecution;
import com.cp.compiler.services.ux.ExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequestMapping("/api")
@RestController
public class ProblemExecutionController {
    
    private ExecutionService executionService;
    
    public ProblemExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }
    
    @PostMapping("/execute")
    public ResponseEntity<RemoteCodeCompilerResponse> execute(@RequestBody ProblemExecution problemExecution) throws IOException {
        log.info("new request, problemId = {}, language = {}, sourceCode = {}",
                problemExecution.getProblemId(),
                problemExecution.getLanguage(),
                problemExecution.getSourceCode());
        return executionService.execute(problemExecution);
    }
}
