package com.cp.compiler.services.businesslogic;

import lombok.Getter;

/**
 * The type Compiler service decorator.
 * Add behaviors to the Compiler service
 *
 * @author Zakaria Maaraki
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
