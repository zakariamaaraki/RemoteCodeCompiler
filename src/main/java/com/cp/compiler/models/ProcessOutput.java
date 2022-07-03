package com.cp.compiler.models;

import lombok.Builder;
import lombok.Getter;

/**
 * The type Process output.
 */
@Builder
@Getter
public class ProcessOutput {
    
    private String stdOut;
    
    private String stdErr;

    private long executionDuration;
    
    private int status;

}
