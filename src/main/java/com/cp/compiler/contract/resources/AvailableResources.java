package com.cp.compiler.contract.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * The type Available resources.
 * Used to know how much CPUs and allowed incoming requests available.
 *
 * @author Zakaria Maaraki
 */
@Builder
@Getter
@EqualsAndHashCode
public class AvailableResources {
    
    @ApiModelProperty(notes = "Available Cpus")
    @JsonProperty("availableCpus")
    private float availableCpus;
    
    @ApiModelProperty(notes = "The maximum number of executions")
    @JsonProperty("maxNumberOfExecutions")
    private int maxNumberOfExecutions;
    
    @ApiModelProperty(notes = "The current number of executions")
    @JsonProperty("currentExecutions")
    private int currentExecutions;
}
