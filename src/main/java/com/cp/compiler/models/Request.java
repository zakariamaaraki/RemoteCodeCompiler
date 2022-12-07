package com.cp.compiler.models;

import com.cp.compiler.mappers.TestCaseMapper;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.testcases.TestCase;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The type Request.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    
    /**
     * The Source code.
     */
    @ApiModelProperty(notes = "The sourcecode")
    @NonNull
    @JsonProperty("sourcecode")
    protected String sourcecode;
    
    /**
     * The Language.
     */
    @ApiModelProperty(notes = "The programming language")
    @NonNull
    @JsonProperty("language")
    protected Language language;
    
    /**
     * The Time limit.
     */
    @ApiModelProperty(notes = "The time limit in sec")
    @NonNull
    @JsonProperty("timeLimit")
    protected int timeLimit;
    
    /**
     * The Memory limit.
     */
    @ApiModelProperty(notes = "The memory limit")
    @NonNull
    @JsonProperty("memoryLimit")
    protected int memoryLimit;
    
    /**
     * The Test cases.
     */
    @ApiModelProperty(notes = "The test cases")
    @NonNull
    @JsonProperty("testCases")
    protected LinkedHashMap<String, TestCase> testCases; // Note: test cases should be given in order
    
    /**
     * Gets source code.
     *
     * @return the source code
     * @throws IOException the io exception
     */
    public MultipartFile getSourcecode() throws IOException {
        return new MockMultipartFile(
                language.getDefaultSourcecodeFileName(),
                language.getDefaultSourcecodeFileName(),
                null,
                new ByteArrayInputStream(this.sourcecode.getBytes()));
    }
    
    /**
     * Gets test cases.
     * This method should be called carefully as it creates a lot of files in memory which have a heavy impact
     * on the memory consumption
     *
     * @return the test cases
     * @throws IOException the io exception
     */
    public List<ConvertedTestCase> getTestCases() throws IOException {
        return TestCaseMapper.toConvertedTestCases(testCases);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Request)) {
            return false;
        }
        
        Request request = (Request) o;
        
        return this.language.equals(request.language)
                && this.sourcecode.equals(request.sourcecode)
                && this.memoryLimit == request.memoryLimit
                && this.timeLimit == request.timeLimit
                && this.testCases.equals(request.testCases);
    }
}
