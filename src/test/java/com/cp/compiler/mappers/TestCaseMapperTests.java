package com.cp.compiler.mappers;

import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.testcases.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestCaseMapperTests {
    
    @Test
    public void shouldConvertTestCaseToConvertedTestCase() throws IOException {
        // Given
        var testCase = new TestCase("input", "expectedOutput");
        String testCaseId = "id";
        
        // When
        ConvertedTestCase convertedTestCase = TestCaseMapper.toConvertedTestCase(testCase, testCaseId);
        
        MultipartFile inputFile = convertedTestCase.getInputFile();
        MultipartFile expectedOutputFile = convertedTestCase.getExpectedOutputFile();
        String input = readFile(new BufferedReader(new InputStreamReader(inputFile.getInputStream())));
        String expectedOutput = readFile(new BufferedReader(new InputStreamReader(expectedOutputFile.getInputStream())));
        
        // Then
        Assertions.assertNotNull(convertedTestCase);
        Assertions.assertEquals(testCaseId, convertedTestCase.getTestCaseId());
        Assertions.assertEquals(testCase.getInput(), input);
        Assertions.assertEquals(testCase.getExpectedOutput(), expectedOutput);
    }
    
    @Test
    public void shouldConvertTestCasesToConvertedTestCases() throws IOException {
        // Given
        var testCase1 = new TestCase("input1", "expectedOutput1");
        var testCase2 = new TestCase("input2", "expectedOutput2");
        var testCase3 = new TestCase("input3", "expectedOutput3");
    
        Map<String, TestCase> testCases = new LinkedHashMap<>() {{
            put("id1", testCase1);
            put("id2", testCase2);
            put("id3", testCase3);
        }};
        
        // When
        List<ConvertedTestCase> convertedTestCases = TestCaseMapper.toConvertedTestCases(testCases);
        
        // Then
        for (ConvertedTestCase convertedTestCase : convertedTestCases) {
            MultipartFile inputFile = convertedTestCase.getInputFile();
            MultipartFile expectedOutputFile = convertedTestCase.getExpectedOutputFile();
            String input = readFile(new BufferedReader(new InputStreamReader(inputFile.getInputStream())));
            String expectedOutput = readFile(new BufferedReader(new InputStreamReader(expectedOutputFile.getInputStream())));

            Assertions.assertNotNull(convertedTestCase);
            Assertions.assertEquals(testCases.get(convertedTestCase.getTestCaseId()).getInput(), input);
            Assertions.assertEquals(testCases.get(convertedTestCase.getTestCaseId()).getExpectedOutput(), expectedOutput);
        }
    }
    
    @Test
    public void convertedTestCasesShouldBeReturnedInOrder() throws IOException {
        // Given
        var testCase1 = new TestCase("input1", "expectedOutput1");
        var testCase2 = new TestCase("input2", "expectedOutput2");
        var testCase3 = new TestCase("input3", "expectedOutput3");
    
        Map<String, TestCase> testCases = new LinkedHashMap<>() {{
            put("id1", testCase1);
            put("id2", testCase2);
            put("id3", testCase3);
        }};
    
        // When
        List<ConvertedTestCase> convertedTestCases = TestCaseMapper.toConvertedTestCases(testCases);
        
        // Then
        int index = 0;
        for (String testCaseId : testCases.keySet()) {
            Assertions.assertEquals(testCaseId, convertedTestCases.get(index).getTestCaseId());
            index++;
        }
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
