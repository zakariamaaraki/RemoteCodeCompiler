package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import org.springframework.http.ResponseEntity;

/**
 * The interface Compiler service.
 *
 * @author: Zakaria Maaraki
 */
public interface CompilerService {
    
    /**
     * Compile response entity.
     *
     * @param execution the execution
     * @return a ResponseEntity containing the result of the execution
     */
    ResponseEntity compile(Execution execution);
}
