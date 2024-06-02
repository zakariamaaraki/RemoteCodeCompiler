package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Throttling exception.
 * It's a monitored exception, and it's a retryable error.
 *
 * @author Zakaria Maaraki
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class CompilerThrottlingException extends MonitoredException {
    
    private static final int RETRY_IN = 10000;
    
    /**
     * Instantiates a new Throttling exception.
     *
     * @param message the message
     */
    public CompilerThrottlingException(String message) {
        super(message, ErrorCode.THROTTLING_ERROR, ErrorType.WARNING, true, RETRY_IN);
    }

    public CompilerThrottlingException(String message, int retryIn ) {
        super(message, ErrorCode.THROTTLING_ERROR, ErrorType.WARNING, true, retryIn);
    }
}