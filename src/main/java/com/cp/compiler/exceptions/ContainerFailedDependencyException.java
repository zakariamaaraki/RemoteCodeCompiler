package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Container dependency exception.
 *
 * @author Zakaria Maaraki
 */
@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class ContainerFailedDependencyException extends RuntimeException {
    
    /**
     * Instantiates a new Container dependency exception.
     */
    public ContainerFailedDependencyException(String message) { super(message); }
}
