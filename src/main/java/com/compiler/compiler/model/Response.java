package com.compiler.compiler.model;

import java.time.LocalDateTime;

public class Response {
	
	private String output;
	private String expectedOutput;
	private int time;
	private int memory;
	private String status;
	private boolean result;
	private LocalDateTime date;
	
	public Response() {}
	
	public Response(String output, String expectedOutput, int time, int memory, String status, boolean result, LocalDateTime date) {
		this.output = output;
		this.expectedOutput = expectedOutput;
		this.time = time;
		this.memory = memory;
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
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public int getMemory() {
		return memory;
	}
	
	public void setMemory(int memory) {
		this.memory = memory;
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
