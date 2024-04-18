package com.cp.compiler.contract;

import com.cp.compiler.contract.testcases.TestCaseResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

// Note: Before changing this class make sure it will not introduce a breaking change for users!!

/**
 * The type Response.
 * This represents the response returned to the user.
 *
 * @author Zakaria Maaraki
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(description = "The returned response")
public class RemoteCodeCompilerExecutionResponse
{
    
    /**
     * Instantiates a new Response.
     *
     * @param verdict             the statusResponse
     * @param statusCode          the status code
     * @param error               the error
     * @param testCasesResult     the test cases result
     * @param compilationDuration the compilation duration
     * @param timeLimit           the time limit
     * @param memoryLimit         the memory limit
     * @param language            the language
     * @param localDateTime       the local date time
     */
    public RemoteCodeCompilerExecutionResponse(String verdict,
                                               int statusCode,
                                               String error,
                                               LinkedHashMap<String, TestCaseResult> testCasesResult,
                                               int compilationDuration,
                                               int timeLimit,
                                               int memoryLimit,
                                               Language language,
                                               LocalDateTime localDateTime) {
        this.verdict = verdict;
        this.statusCode = statusCode;
        this.error = error;
        this.testCasesResult = testCasesResult;
        this.compilationDuration = compilationDuration;
        this.timeLimit = timeLimit * 1000; // convert timeLimit to milliSec
        this.memoryLimit = memoryLimit;
        this.language = language;
        this.dateTime = localDateTime;
        this.averageExecutionDuration = computeTheAverageExecutionDuration(testCasesResult);
    }
    
    @ApiModelProperty(notes = "The statusResponse")
    private String verdict;
    
    @ApiModelProperty(notes = "The corresponding status code of the statusResponse")
    private int statusCode;
    
    @ApiModelProperty(notes = "An error if it occurs")
    private String error;
    
    @ApiModelProperty(notes = "The result of each test case")
    private LinkedHashMap<String, TestCaseResult> testCasesResult; // Should be returned in order
    
    @ApiModelProperty(notes = "The compilation duration")
    private int compilationDuration;
    
    @ApiModelProperty(notes = "The average execution duration")
    private float averageExecutionDuration;
    
    @ApiModelProperty(notes = "The execution time limit")
    private int timeLimit;
    
    @ApiModelProperty(notes = "The execution memory limit")
    private int memoryLimit;
    
    @ApiModelProperty(notes = "The programming language")
    private Language language;
    
    @EqualsAndHashCode.Exclude
    @ApiModelProperty(notes = "The dateTime of the execution")
    private LocalDateTime dateTime;

    private float computeTheAverageExecutionDuration(Map<String, TestCaseResult> testCasesResult) {
        float sum = 0;
        for (TestCaseResult testCaseResult : testCasesResult.values()) {
            if (testCaseResult.getExecutionDuration() == 0) {
                continue;
            }
            sum += testCaseResult.getExecutionDuration();
        }
        return sum / testCasesResult.size();
    }
}
