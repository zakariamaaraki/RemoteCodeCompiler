package com.cp.compiler.services;

import lombok.Getter;

public abstract class CompilerServiceDecorator implements CompilerService {
    
    @Getter
    private CompilerService compilerService;
    
    protected CompilerServiceDecorator(CompilerService compilerService) {
        this.compilerService = compilerService;
    }
}
