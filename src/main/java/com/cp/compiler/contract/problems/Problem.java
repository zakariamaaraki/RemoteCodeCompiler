package com.cp.compiler.contract.problems;

import com.cp.compiler.contract.testcases.TestCase;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    
    @ApiModelProperty(notes = "The problem's id")
    @JsonProperty("id")
    private long id;
    
    @ApiModelProperty(notes = "The title of the problem")
    @JsonProperty("title")
    private String title;
    
    @ApiModelProperty(notes = "The description of the problem")
    @JsonProperty("description")
    private String description;
    
    @ApiModelProperty(notes = "The time limit of the execution")
    @JsonProperty("timeLimit")
    private int timeLimit;
    
    @ApiModelProperty(notes = "The memory limit of the execution")
    @JsonProperty("memoryLimit")
    private int memoryLimit;
    
    @ApiModelProperty(notes = "The list of test cases")
    @JsonProperty("testCases")
    private List<TestCase> testCases;
    
    @ApiModelProperty(notes = "The list of tags")
    @JsonProperty("tags")
    private List<String> tags;
    
    @ApiModelProperty(notes = "The difficulty of the problem")
    @JsonProperty("difficulty")
    private Difficulty difficulty;
}
