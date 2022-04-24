package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerServerException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The interface Compiler service.
 *
 * @author: Zakaria Maaraki
 */
public interface CompilerService {
    
    /**
     * Compile
     *
     * @param request object
     * @return a ResponseEntity containing the result of the execution
     * @throws CompilerServerException error from the server
     * @throws IOException             the io exception
     */
    ResponseEntity<Object> compile(Request request) throws CompilerServerException, IOException;
    
    /**
     * Compile response entity.
     *
     * @param execution the execution
     * @return a ResponseEntity containing the result of the execution
     * @throws CompilerServerException error from the server
     */
    ResponseEntity<Object> compile(Execution execution) throws CompilerServerException;
    
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
}
