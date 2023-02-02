package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Compiler bad request.
 * It's a monitored exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CompilerBadRequestException extends MonitoredException {
    
    /**
     * Instantiates a new CompilerBadRequest exception.
     *
     * @param message the message
     */
    public CompilerBadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST, ErrorType.WARNING);
    }
}
