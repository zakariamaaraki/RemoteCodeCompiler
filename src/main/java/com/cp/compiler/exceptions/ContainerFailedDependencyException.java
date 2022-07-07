package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Container dependency exception.
 */
@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class ContainerFailedDependencyException extends RuntimeException {
    
    /**
     * Instantiates a new Container dependency exception.
     */
    public ContainerFailedDependencyException() { super("Error in a dependency : Container Engine"); }
}
