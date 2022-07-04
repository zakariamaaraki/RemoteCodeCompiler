package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Process execution exception.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ProcessExecutionException extends RuntimeException {
    
    /**
     * Instantiates a new Process execution exception.
     *
     * @param message the message
     */
    public ProcessExecutionException(String message) {
        super(message);
    }
}
