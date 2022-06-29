package com.cp.compiler.models;

import lombok.Builder;
import lombok.Getter;

/**
 * The type Container output.
 */
@Builder
public class ContainerOutput {
    
    @Getter
    private String stdOut;
    
    @Getter
    private String stdErr;
    
    @Getter
    private long executionDuration;
    
    @Getter
    private int status;
}
