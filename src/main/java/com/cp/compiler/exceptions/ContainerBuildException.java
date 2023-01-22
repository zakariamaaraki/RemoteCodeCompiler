package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Container Build Exception Class.
 * It's a monitored exception.
 *
 * @author Zakaria Maaraki
 */
@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class ContainerBuildException extends MonitoredException {
    
    /**
     * Instantiates a new Container build exception.
     *
     * @param message the message
     */
    public ContainerBuildException(String message) {
        super(message, ErrorCode.CONTAINER_BUILD_ERROR, ErrorType.ERROR);
    }
}
