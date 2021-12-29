package com.cp.compiler.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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
	
	@SneakyThrows
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof Request)) {
			return false;
		}
		Request request = (Request) o;

		if (request.input != this.input && ((this.input != null && !this.input.equals("")) || (request.input != null && !request.input.equals("")))) {
			return false;
		}
		
		return this.language.equals(request.language)
				&& this.expectedOutput.equals(request.expectedOutput)
				&& this.memoryLimit == request.memoryLimit
				&& this.timeLimit == request.timeLimit
				&& this.sourceCode.equals(this.sourceCode);
	}
}
