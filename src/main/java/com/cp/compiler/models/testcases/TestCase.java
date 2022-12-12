package com.cp.compiler.models.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * The Test case.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class TestCase {
    
    @ApiModelProperty(notes = "The input, can be null")
    @JsonProperty("input")
    private String input;
    
    @ApiModelProperty(notes = "The expected output, can not be null")
    @NonNull
    @JsonProperty("expectedOutput")
    private String expectedOutput;
}
