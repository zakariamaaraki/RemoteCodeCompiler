package com.cp.compiler.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Result.
 *
 * @author Zakaria Maaraki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "The result of the execution")
public class Result {
    
    /**
     * Instantiates a new Result.
     *
     * @param verdict           the verdict
     * @param output            the output
     * @param error             the error
     * @param expectedOutput    the expected output
     * @param executionDuration the execution duration
     */
    public Result(Verdict verdict, String output, String error, String expectedOutput, long executionDuration) {
        this.statusCode = verdict.getStatusCode();
        this.statusResponse = verdict.getStatusResponse();
        this.output = output;
        this.error = error;
        this.expectedOutput = expectedOutput;
        this.executionDuration = executionDuration;
    }
    
    @ApiModelProperty(notes = "The value can be one of these : Accepted, Wrong Answer, " +
            "Compilation Error, Runtime Error, Out Of Memory, Time Limit Exceeded")
    private String statusResponse;
    
    @ApiModelProperty(notes = "The corresponding status code of the status response")
    private int statusCode;
    
    @ApiModelProperty(notes = "The output of the program during the execution")
    private String output;
    
    @ApiModelProperty(notes = "The error if it occurs")
    private String error;
    
    @ApiModelProperty(notes = "The expected output")
    private String expectedOutput;
    
    @ApiModelProperty(notes = "The execution duration in ms")
    private long executionDuration;
}
