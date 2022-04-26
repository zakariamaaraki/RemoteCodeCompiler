package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Request;
import org.springframework.http.ResponseEntity;

/**
 * The interface Compiler service.
 *
 * @author: Zakaria Maaraki
 */
public interface CompilerService {
    
    /**
     * Gets max execution memory.
     *
     * @return the max execution memory
     */
    int getMaxExecutionMemory();
    
    /**
     * Gets min execution memory.
     *
     * @return the min execution memory
     */
    int getMinExecutionMemory();
    
    /**
     * Gets max execution time.
     *
     * @return the max execution time
     */
    int getMaxExecutionTime();
    
    /**
     * Gets min execution time.
     *
     * @return the min execution time
     */
    int getMinExecutionTime();
    
    /**
     * Is delete docker image boolean.
     *
     * @return the boolean
     */
    boolean isDeleteDockerImage();
    
    /**
     * Compile
     *
     * @param request object
     * @return a ResponseEntity containing the result of the execution
     * @throws Exception the exception
     */
    ResponseEntity<Object> compile(Request request) throws Exception;
    
    /**
     * Compile response entity.
     *
     * @param execution the execution
     * @return a ResponseEntity containing the result of the execution
     * @throws Exception the exception
     */
    ResponseEntity<Object> compile(Execution execution) throws Exception;
}
