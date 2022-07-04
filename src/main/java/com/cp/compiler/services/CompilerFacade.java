package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import org.springframework.http.ResponseEntity;

/**
 * The interface Compiler facade.
 */
public interface CompilerFacade {
    
    /**
     * Compile response entity.
     *
     * @param execution     the execution
     * @param isLongRunning the is long running
     * @param url           the url
     * @return the response entity
     * @throws Exception the exception
     */
    ResponseEntity compile(Execution execution, boolean isLongRunning, String url);
}
