package com.cp.compiler.models;

import com.cp.compiler.mappers.TestCaseMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        List<ConvertedTestCase> convertedTestCases = request.getTestCases();
        
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
    public void equalsShouldReturnTrue() {
        // Given
        var testCases = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test2", new TestCase("input", "expectedOutput"));
        }};
    
        var request1 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
    
        var request2 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
        
        // When
        boolean equals = request1.equals(request2);
        
        // Then
        Assertions.assertTrue(equals);
    }
    
    @Test
    public void ifLanguageIsDifferentEqualsShouldReturnFalse() {
        // Given
        var testCases = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test2", new TestCase("input", "expectedOutput"));
        }};
        
        var request1 = new Request(
                "sourceCode",
                Language.PYTHON,
                15,
                500,
                testCases);
        
        var request2 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
        
        // When
        boolean equals = request1.equals(request2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    public void ifSourcecodeIsDifferentEqualsShouldReturnFalse() {
        // Given
        var testCases = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test2", new TestCase("input", "expectedOutput"));
        }};
        
        var request1 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
        
        var request2 = new Request(
                "sourceCode2",
                Language.JAVA,
                15,
                500,
                testCases);
        
        // When
        boolean equals = request1.equals(request2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    public void ifTimeLimitIsDifferentEqualsShouldReturnFalse() {
        // Given
        var testCases = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test2", new TestCase("input", "expectedOutput"));
        }};
        
        var request1 = new Request(
                "sourceCode",
                Language.JAVA,
                10,
                500,
                testCases);
        
        var request2 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
        
        // When
        boolean equals = request1.equals(request2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    public void ifMemoryIsDifferentEqualsShouldReturnFalse() {
        // Given
        var testCases = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test2", new TestCase("input", "expectedOutput"));
        }};
        
        var request1 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases);
        
        var request2 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                400,
                testCases);
        
        // When
        boolean equals = request1.equals(request2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    public void ifTestCasesAreDifferentEqualsShouldReturnFalse() {
        // Given
        var testCases1 = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test2", new TestCase("input", "expectedOutput"));
        }};
        
        var testCases2 = new LinkedHashMap<String, TestCase>() {{
            put("test1", new TestCase("input", "expectedOutput"));
            put("test3", new TestCase("input3", "expectedOutput3"));
        }};
        
        
        var request1 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases1);
        
        var request2 = new Request(
                "sourceCode",
                Language.JAVA,
                15,
                500,
                testCases2);
        
        // When
        boolean equals = request1.equals(request2);
        
        // Then
        Assertions.assertFalse(equals);
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
