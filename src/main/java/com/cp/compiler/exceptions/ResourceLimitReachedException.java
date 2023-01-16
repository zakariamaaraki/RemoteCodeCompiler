package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Resource limit reached exception.
 */
@ResponseStatus(HttpStatus.LOOP_DETECTED)
public class ResourceLimitReachedException extends RuntimeException {
    
    /**
     * Instantiates a new Resource limit reached exception.
     *
     * @param message the message
     */
    public ResourceLimitReachedException(String message) {
        super(message);
    }
}
