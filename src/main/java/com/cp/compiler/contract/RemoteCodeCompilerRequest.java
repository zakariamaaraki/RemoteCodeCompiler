package com.cp.compiler.contract;

import com.cp.compiler.mappers.TestCaseMapper;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.testcases.TestCase;
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
 *
 * @author Zakaria Maaraki
 */
@Getter
@Setter // used by mapstruct
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@AllArgsConstructor
public class RemoteCodeCompilerRequest {
    
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
    public MultipartFile getSourcecodeFile() throws IOException {
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
    public List<TransformedTestCase> getConvertedTestCases() throws IOException {
        return TestCaseMapper.toConvertedTestCases(testCases);
    }
}
