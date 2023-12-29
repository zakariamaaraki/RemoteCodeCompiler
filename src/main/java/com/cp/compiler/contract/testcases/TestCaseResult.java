package com.cp.compiler.contract.testcases;

import com.cp.compiler.models.Verdict;
import com.cp.compiler.utils.DiffComputer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

/**
 * The type Result.
 *
 * @author Zakaria Maaraki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(description = "The result of the execution")
public class TestCaseResult {
    
    /**
     * Instantiates a new Test case result.
     *
     * @param statusResponse    the status response
     * @param output            the output
     * @param error             the error
     * @param expectedOutput    the expected output
     * @param executionDuration the execution duration
     */
    public TestCaseResult(Verdict statusResponse,
                          String output,
                          String error,
                          String expectedOutput,
                          int executionDuration) {
        this.verdict = statusResponse;
        this.verdictStatusCode = statusResponse.getStatusCode();
        this.statusResponse = statusResponse.getStatusResponse();
        this.output = output;
        this.error = error;
        this.expectedOutput = expectedOutput;
        this.executionDuration = executionDuration;

        if (statusResponse != Verdict.ACCEPTED) {
            // compute the diff between the output and the expected output
            outputDiff = DiffComputer.diff(output, expectedOutput);
        }
    }
    
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ApiModelProperty(notes = "The verdict enum")
    private Verdict verdict;
    
    @ApiModelProperty(notes = "The value can be one of these : Accepted, Wrong Answer, " +
            "Compilation Error, Runtime Error, Out Of Memory, Time Limit Exceeded")
    @JsonProperty("verdict")
    private String statusResponse;
    
    @ApiModelProperty(notes = "The corresponding status code of the status response")
    @JsonProperty("verdictStatusCode")
    private int verdictStatusCode;
    
    @ApiModelProperty(notes = "The output of the program during the execution")
    @JsonProperty("output")
    private String output;
    
    @ApiModelProperty(notes = "The error if it occurs")
    @JsonProperty("error")
    private String error;
    
    @ApiModelProperty(notes = "The expected output")
    @JsonProperty("expectedOutput")
    private String expectedOutput;
    
    @ApiModelProperty(notes = "The execution duration in ms")
    @JsonProperty("executionDuration")
    private int executionDuration;

    @ApiModelProperty(notes = "The diff between the output and the expected output")
    @JsonProperty("outputDiff")
    private String outputDiff;
}
