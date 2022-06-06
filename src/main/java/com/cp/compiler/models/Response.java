package com.cp.compiler.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The type Response.
 *
 * @author Zakaria Maaraki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "The returned response")
public class Response {
    
    @ApiModelProperty(notes = "The result object")
    private Result result;

    @ApiModelProperty(notes = "The dateTime of the execution")
    private LocalDateTime dateTime;
}
