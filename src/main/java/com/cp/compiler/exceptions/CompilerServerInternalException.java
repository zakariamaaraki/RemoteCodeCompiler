package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Compiler server exception.
 * It's a monitored exception, and it's a retryable error.
 *
 * @author: Zakaria Maaraki
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CompilerServerInternalException extends MonitoredException {
    
    private static final int RETRY_IN = 60000;
    
    /**
     * Instantiates a new Compiler server exception.
     *
     * @param message the message
     */
    public CompilerServerInternalException(String message) {
        super(message, ErrorCode.COMPILER_SERVER_INTERNAL_ERROR, ErrorType.ERROR, true, RETRY_IN);
    }
}
