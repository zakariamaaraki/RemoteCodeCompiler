package com.cp.compiler.models.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * The type Available resources.
 */
@Builder
@Getter
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvailableResources)) {
            return false;
        }
        
        AvailableResources that = (AvailableResources) o;
        
        return Float.compare(that.getAvailableCpus(), getAvailableCpus()) == 0
                && getMaxNumberOfExecutions() == that.getMaxNumberOfExecutions()
                && getCurrentExecutions() == that.getCurrentExecutions();
    }
}
