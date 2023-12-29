package com.cp.compiler.models;

import com.cp.compiler.contract.testcases.TestCaseResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.LinkedHashMap;

/**
 * The type Execution response.
 *
 * @author Zakaria Maaraki
 */
@Getter
@EqualsAndHashCode
@Builder
public class ExecutionResponse {
    
    @ApiModelProperty(notes = "The verdict")
    private Verdict verdict;
    
    @ApiModelProperty(notes = "The result of each test case")
    private LinkedHashMap<String, TestCaseResult> testCasesResult;
    
    @ApiModelProperty(notes = "The error if it occurs")
    private String error;
}
