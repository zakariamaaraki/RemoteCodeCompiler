package com.cp.compiler.models.containers;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type ContainerInfo.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContainerInfo {
    
    @ApiModelProperty(notes = "Container status")
    private String status;
    
    @ApiModelProperty(notes = "Container creation time")
    private LocalDateTime creationTime;
    
    @ApiModelProperty(notes = "Error")
    private String error;
    
    @ApiModelProperty(notes = "Exit code")
    private int exitCode;
    
    @ApiModelProperty(notes = "Container start time")
    private LocalDateTime startTime;
    
    @ApiModelProperty(notes = "Container end time")
    private LocalDateTime endTime;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContainerInfo)) {
            return false;
        }
        
        ContainerInfo that = (ContainerInfo) o;
        return getExitCode() == that.getExitCode()
                && Objects.equals(getStatus(), that.getStatus())
                && Objects.equals(getCreationTime(), that.getCreationTime())
                && Objects.equals(getError(), that.getError())
                && Objects.equals(getStartTime(), that.getStartTime())
                && Objects.equals(getEndTime(), that.getEndTime());
    }
}
