package com.cp.compiler.models.processes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Process output.
 *
 * @author Zakaria Maaraki
 */
@Builder
@Getter
public class ProcessOutput {
    
    private String stdOut;
    
    @Setter
    private String stdErr;

    private int executionDuration;
    
    private int status;
}
