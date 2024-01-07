package com.cp.compiler.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class RemoteCodeCompilerResponse extends BaseRemoteCodeCompilerResponse {
    
    @ApiModelProperty(notes = "The execution response")
    private RemoteCodeCompilerExecutionResponse execution;
}
