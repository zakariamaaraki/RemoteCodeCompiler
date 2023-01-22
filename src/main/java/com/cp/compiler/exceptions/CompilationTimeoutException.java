package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Execution timeout exception.
 * It's a monitored exception.
 *
 * @author Zakaria Maaraki
 */
@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class CompilationTimeoutException extends MonitoredException {
    
    /**
     * Instantiates a new Compilation step timeout exception.
     *
     * @param message the message
     */
    public CompilationTimeoutException(String message) {
        super(message, ErrorCode.COMPILATION_TIMEOUT_ERROR, ErrorType.WARNING);
    }
}
