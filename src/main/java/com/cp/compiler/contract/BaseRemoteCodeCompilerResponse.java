package com.cp.compiler.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class BaseRemoteCodeCompilerResponse {
    
    @ApiModelProperty(notes = "The error message")
    private String error;
}
