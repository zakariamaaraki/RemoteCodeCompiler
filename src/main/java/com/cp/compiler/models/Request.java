package com.cp.compiler.models;

import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    
    @JsonProperty("input")
    protected String input;
    
    @NonNull
    @JsonProperty("expectedOutput")
    protected String expectedOutput;
    
    @NonNull
    @JsonProperty("sourceCode")
    protected String sourceCode;
    
    @NonNull
    @JsonProperty("language")
    protected Language language;
    
    @NonNull
    @JsonProperty("timeLimit")
    protected int timeLimit;
    
    @NonNull
    @JsonProperty("memoryLimit")
    protected int memoryLimit;
    
    public MultipartFile getSourceCode() throws IOException {
        return new MockMultipartFile(
                language.getSourceCodeFileName(),
                language.getSourceCodeFileName(),
                null,
                new ByteArrayInputStream(this.sourceCode.getBytes()));
    }
    
    public MultipartFile getExpectedOutput() throws IOException {
        return new MockMultipartFile(
                WellKnownFiles.EXPECTED_OUTPUT_FILE_NAME,
                WellKnownFiles.EXPECTED_OUTPUT_FILE_NAME,
                null,
                new ByteArrayInputStream(this.expectedOutput.getBytes()));
    }
    
    public MultipartFile getInput() throws IOException {
        if (this.input == null) {
            return null;
        }
        return new MockMultipartFile(
                WellKnownFiles.INPUT_FILE_NAME,
                WellKnownFiles.INPUT_FILE_NAME,
                null,
                new ByteArrayInputStream(this.input.getBytes()));
    }
    
    @SneakyThrows
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Request)) {
            return false;
        }
        
        Request request = (Request) o;
        
        if (request.input != this.input
                && ((this.input != null && !this.input.equals(""))
                || (request.input != null && !request.input.equals("")))) {
            return false;
        }
        
        return this.language.equals(request.language)
                && this.expectedOutput.equals(request.expectedOutput)
                && this.memoryLimit == request.memoryLimit
                && this.timeLimit == request.timeLimit
                && this.sourceCode.equals(this.sourceCode);
    }
}
