package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Throttling exception.
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class ThrottlingException extends RuntimeException {
    
    /**
     * Instantiates a new Throttling exception.
     *
     * @param message the message
     */
    public ThrottlingException(String message) {
        super(message);
    }
}