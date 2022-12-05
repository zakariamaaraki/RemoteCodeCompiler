package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * The interface Compiler facade.
 */
public interface CompilerFacade {
    
    /**
     * Compile response entity.
     *
     * @param execution       the execution
     * @param isLongRunning   the is long running
     * @param url             the url
     * @param customDimension the custom dimension
     * @return the response entity
     * @throws IOException the io exception
     */
    ResponseEntity compile(Execution execution, boolean isLongRunning, String url, String customDimension) throws IOException;
}
