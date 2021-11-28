package com.cp.compiler.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Result.
 *
 * @author Zakaria Maaraki
 */
@Data
@AllArgsConstructor
@ApiModel(description = "The result of the execution")
public class Result {
	
	@ApiModelProperty(notes = "The value can be one of these : Accepted, Wrong Answer, Compilation Error, Runtime Error, Out Of Memory, Time Limit Exceeded")
	private String verdict;
	
	@ApiModelProperty(notes = "The output of the program during the execution")
	private String output;
	
	@ApiModelProperty(notes = "The expected output")
	private String expectedOutput;
	
}
