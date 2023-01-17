package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Resource limit reached exception.
 */
@ResponseStatus(HttpStatus.LOOP_DETECTED)
public class ResourceLimitReachedException extends MonitoredException {
    
    /**
     * Instantiates a new Resource limit reached exception.
     *
     * @param message the message
     */
    public ResourceLimitReachedException(String message) {
        super(message, ErrorCode.RESOURCE_LIMIT_REACHED_ERROR, ErrorType.WARNING);
    }
}