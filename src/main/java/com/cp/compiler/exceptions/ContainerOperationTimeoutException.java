package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Container operation timeout exception.
 */
@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class ContainerOperationTimeoutException extends ProcessExecutionTimeoutException {
    
    /**
     * Instantiates a new Container operation timeout exception.
     *
     * @param message the message
     */
    public ContainerOperationTimeoutException(String message) {
        super(message);
    }
}
