package com.compiler.compiler.model;

import java.time.LocalDateTime;

public class Response {
	
	private String output;
	private String expectedOutput;
	private String status;
	private boolean result;
	private LocalDateTime date;
	
	public Response() {}
	
	public Response(String output, String expectedOutput, String status, boolean result, LocalDateTime date) {
		this.output = output;
		this.expectedOutput = expectedOutput;
		this.status = status;
		this.result = result;
		this.date = date;
	}
	
	public String getOutput() {
		return output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public String getExpectedOutput() {
		return expectedOutput;
	}
	
	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isResult() {
		return result;
	}
	
	public void setResult(boolean result) {
		this.result = result;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}
