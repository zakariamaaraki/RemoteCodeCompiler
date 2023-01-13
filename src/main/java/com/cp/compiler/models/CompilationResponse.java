package com.cp.compiler.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class CompilationResponse {
    
    @ApiModelProperty(notes = "The verdict")
    private Verdict verdict;
    
    @ApiModelProperty(notes = "The compilation duration")
    private int compilationDuration;
    
    @ApiModelProperty(notes = "The error if it occurs")
    private String error;
}
