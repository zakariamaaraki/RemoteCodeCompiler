package com.cp.compiler.models;

import com.cp.compiler.models.testcases.TestCaseResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Response.
 *
 * @author Zakaria Maaraki
 */
@Data
@NoArgsConstructor
@ApiModel(description = "The returned response")
public class Response {
    
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
    public Response(String verdict,
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
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Response)) {
            return false;
        }
        
        Response response = (Response) o;
        
        return this.error.equals(response.getError())
                && this.memoryLimit == response.getMemoryLimit()
                && this.timeLimit == response.getTimeLimit()
                && this.language.equals(response.getLanguage())
                && this.testCasesResult.equals(response.getTestCasesResult())
                && this.averageExecutionDuration == response.getAverageExecutionDuration()
                && this.statusCode == response.getStatusCode()
                && this.verdict.equals(response.getVerdict())
                && this.compilationDuration == response.getCompilationDuration();
    }
}
