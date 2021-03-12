package com.cp.compiler.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ApiModel(description = "The returned response")
public class Response {
	
	@ApiModelProperty(notes = "The output of the program during the execution")
	private String output;
	
	@ApiModelProperty(notes = "The expected output")
	private String expectedOutput;
	
	@ApiModelProperty(notes = "The verdict can be one of these : Accepted, Wrong Answer, Compilation Error, Runtime Error, Out Of Memory, Time Limit Exceeded")
	private String status;
	
	@ApiModelProperty(notes = "The date of the execution")
	private LocalDateTime date;

}
