package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Container execution timeout exception.
 */
@ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
public class ContainerOperationTimeoutException extends ProcessExecutionTimeoutException {
    
    /**
     * Instantiates a new Container execution timeout exception.
     */
    public ContainerOperationTimeoutException() {
        super("The container execution exceeded the maximum time allowed for its execution");
    }
}
