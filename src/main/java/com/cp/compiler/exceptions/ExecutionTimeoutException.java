package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Execution timeout exception.
 */
@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class ExecutionTimeoutException extends RuntimeException {
    
    /**
     * Instantiates a new Compilation step timeout exception.
     *
     * @param message the message
     */
    public ExecutionTimeoutException(String message) {
        super(message);
    }
}
