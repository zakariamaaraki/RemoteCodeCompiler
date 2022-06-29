package com.cp.compiler.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Factory not found exception.
 */
@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class FactoryNotFoundException extends RuntimeException {
    
    /**
     * Instantiates a new Factory not found exception.
     *
     * @param message the message
     */
    public FactoryNotFoundException(String message) {
        super(message);
    }
}
