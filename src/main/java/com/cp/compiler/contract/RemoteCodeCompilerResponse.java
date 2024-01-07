package com.cp.compiler.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class RemoteCodeCompilerResponse extends BaseRemoteCodeCompilerResponse {
    
    @ApiModelProperty(notes = "The execution response")
    private RemoteCodeCompilerExecutionResponse execution;
}
