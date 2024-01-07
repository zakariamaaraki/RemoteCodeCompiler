package com.cp.compiler.services.api;

import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.executions.Execution;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * The interface Compiler facade.
 *
 * @author Zakaria Maaraki
 */
public interface CompilerFacade {
    
    /**
     * Compile response entity.
     *
     * @param execution     the execution
     * @param isLongRunning the is long running
     * @param url           the url
     * @param userId        the user id
     * @return the response entity
     * @throws IOException the io exception
     */
    ResponseEntity<RemoteCodeCompilerResponse> compile(Execution execution, boolean isLongRunning, String url, String userId) throws IOException;
}
