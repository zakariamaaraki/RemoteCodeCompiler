package com.cp.compiler.services;

import lombok.Getter;

/**
 * The type Compiler service decorator.
 * Add behavior to the Compiler service
 */
public abstract class CompilerServiceDecorator implements CompilerService {
    
    @Getter
    private CompilerService compilerService;
    
    /**
     * Instantiates a new Compiler service decorator.
     *
     * @param compilerService the compiler service
     */
    protected CompilerServiceDecorator(CompilerService compilerService) {
        this.compilerService = compilerService;
    }
}
