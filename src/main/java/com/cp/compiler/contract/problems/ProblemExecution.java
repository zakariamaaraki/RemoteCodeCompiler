package com.cp.compiler.contract.problems;

import com.cp.compiler.contract.Language;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProblemExecution {
    
    @ApiModelProperty(notes = "The problem's id")
    @JsonProperty("problemId")
    private long problemId;
    
    @ApiModelProperty(notes = "The source code")
    @JsonProperty("sourceCode")
    private String sourceCode;
    
    @ApiModelProperty(notes = "The programming language")
    @JsonProperty("language")
    private Language language;
}
