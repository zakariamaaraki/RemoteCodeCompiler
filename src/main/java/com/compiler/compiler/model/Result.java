package com.compiler.compiler.model;

public class Result {
	
	private String verdict;
	private String output;
	private String expectedOutput;
	
	public Result(String verdict, String output, String expectedOutput) {
		this.verdict = verdict;
		this.output = output;
		this.expectedOutput = expectedOutput;
	}
	
	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	
	public String getVerdict() {
		return verdict;
	}
	
	public String getOutput() {
		return output;
	}
	
	public String getExpectedOutput() {
		return expectedOutput;
	}
}
