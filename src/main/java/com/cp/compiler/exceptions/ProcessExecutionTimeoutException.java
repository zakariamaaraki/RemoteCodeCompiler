package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Process execution timeout exception.
 */
@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class ProcessExecutionTimeoutException extends RuntimeException {
    
    /**
     * Instantiates a new Process execution timeout exception.
     */
    public ProcessExecutionTimeoutException(long timeout) {
        super("The process execution exceeded the maximum time allowed for its execution " + timeout + " seconds");
    }
    
    /**
     * Instantiates a new Process execution timeout exception.
     *
     * @param message the message
     */
    public ProcessExecutionTimeoutException(String message) {
        super(message);
    }
}
