package com.cp.compiler.models.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * The Test case.
 */
@Getter
@AllArgsConstructor
public class TestCase {
    
    @ApiModelProperty(notes = "The input, can be null")
    @JsonProperty("input")
    private String input;
    
    @ApiModelProperty(notes = "The expected output, can not be null")
    @NonNull
    @JsonProperty("expectedOutput")
    private String expectedOutput;
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TestCase)) {
            return false;
        }
    
        var testCase = (TestCase) o;
        
        if (testCase.input != this.input
                && ((this.input != null && !this.input.equals(""))
                || (testCase.input != null && !testCase.input.equals("")))) {
            return false;
        }
        
        return this.expectedOutput.equals(testCase.expectedOutput);
    }
}
