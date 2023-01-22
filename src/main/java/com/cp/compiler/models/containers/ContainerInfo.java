package com.cp.compiler.models.containers;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The type ContainerInfo.
 *
 * @author Zakaria Maaraki
 */
@Data
@EqualsAndHashCode
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
}
