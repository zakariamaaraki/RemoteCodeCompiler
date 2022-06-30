package com.cp.compiler.models;

import lombok.Builder;
import lombok.Getter;

/**
 * The type Process output.
 */
@Builder
public class ProcessOutput {
    
    @Getter
    private String stdOut;
    
    @Getter
    private String stdErr;
    
    @Getter
    private long executionDuration;
    
    @Getter
    private int status;
}
