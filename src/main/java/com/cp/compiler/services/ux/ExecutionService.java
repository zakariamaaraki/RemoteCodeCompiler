package com.cp.compiler.services.ux;

import com.cp.compiler.contract.problems.ProblemExecution;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ExecutionService {
    
    ResponseEntity execute(ProblemExecution problemExecution) throws IOException;
}
