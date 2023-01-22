package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Throttling exception.
 * It's a monitored exception.
 *
 * @author Zakaria Maaraki
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class ThrottlingException extends MonitoredException {
    
    /**
     * Instantiates a new Throttling exception.
     *
     * @param message the message
     */
    public ThrottlingException(String message) {
        super(message, ErrorCode.THROTTLING_ERROR, ErrorType.WARNING);
    }
}