package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Container execution exception.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ContainerExecutionException extends ProcessExecutionException {
    
    /**
     * Instantiates a new Container execution exception.
     *
     * @param message the message
     */
    public ContainerExecutionException(String message) {super(message);}
}
