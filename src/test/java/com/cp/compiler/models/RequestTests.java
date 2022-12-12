package com.cp.compiler.models;

import com.cp.compiler.mappers.TestCaseMapper;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.testcases.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;

public class RequestTests {
    
    @Test
    void getTestCasesShouldReturnConvertedTestCases() throws IOException {
        // Given
        var testCases = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test2", new TestCase("input", "expectedOutput"));
        }};
        
        var request = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
        
        // When
        List<ConvertedTestCase> convertedTestCases = request.getConvertedTestCases();
        
        // Then
        List<ConvertedTestCase> expectedConvertedTestCases = TestCaseMapper.toConvertedTestCases(testCases);
        
        Assertions.assertEquals(expectedConvertedTestCases.size(), convertedTestCases.size());
        
        for (int i = 0; i < convertedTestCases.size(); i++) {
            ConvertedTestCase expectedConvertedTestCase = expectedConvertedTestCases.get(i);
            ConvertedTestCase convertedTestCase = convertedTestCases.get(i);
    
            String expectedInput = readFile(
                    new BufferedReader(
                            new InputStreamReader(expectedConvertedTestCase.getInputFile().getInputStream())));
            String expectedExpectedOutput = readFile(
                    new BufferedReader(
                            new InputStreamReader(expectedConvertedTestCase.getExpectedOutputFile().getInputStream())));
    
            String input = readFile(
                    new BufferedReader(
                            new InputStreamReader(convertedTestCase.getInputFile().getInputStream())));
            String expectedOutput = readFile(
                    new BufferedReader(
                            new InputStreamReader(convertedTestCase.getExpectedOutputFile().getInputStream())));
            
            Assertions.assertEquals(expectedConvertedTestCase.getTestCaseId(), convertedTestCase.getTestCaseId());
            Assertions.assertEquals(expectedInput, input);
            Assertions.assertEquals(expectedExpectedOutput, expectedOutput);
        }
    }
    
    @Test
    void shouldReturnSourceCodeFile() throws IOException {
        // Given
        var testCases = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
        }};
    
        var request = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
        
        // When
        MultipartFile sourceCodeFile = request.getSourcecodeFile();
    
        String sourceCode = readFile(
                new BufferedReader(
                        new InputStreamReader(sourceCodeFile.getInputStream())));
        
        // Then
        Assertions.assertEquals(request.getSourcecode(), sourceCode);
    }
    
    private String readFile(BufferedReader bufferedReader) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder();
        
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }
        
        return builder.toString();
    }
}
