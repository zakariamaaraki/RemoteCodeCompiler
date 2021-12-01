package com.cp.compiler.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
	
	@JsonProperty("input")
	private String input;
	
	@JsonProperty("expectedOutput")
	private String expectedOutput;
	
	@JsonProperty("sourceCode")
	private String sourceCode;
	
	@JsonProperty("language")
	private Language language;
	
	@JsonProperty("timeLimit")
	private int timeLimit;
	
	@JsonProperty("memoryLimit")
	private int memoryLimit;
	
	public MultipartFile getSourceCode() throws IOException {
		File sourceCodeFile = new File(language.getFolder() + "/" + language.getFile());
		return new MockMultipartFile(language.getFile(), language.getFile(), null , new ByteArrayInputStream(this.sourceCode.getBytes()));
	}
	
	public MultipartFile getExpectedOutput() throws IOException {
		File expectedOutput = new File(language.getFolder() + "/expectedOutput.txt");
		return new MockMultipartFile("expectedOutput.txt", "expectedOutput.txt", null ,new ByteArrayInputStream(this.expectedOutput.getBytes()));
	}
	
	public MultipartFile getInput() throws IOException {
		if (this.input == null) {
			return null;
		}
		File input = new File(language.getFolder() + "/input.txt");
		return new MockMultipartFile("input.txt", "input.txt", null ,new ByteArrayInputStream(this.input.getBytes()));
	}
}
