package com.cp.compiler.models;

import com.cp.compiler.models.testcases.TestCaseResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class ResponseTests {
    
    @Test
    void shouldComputeTheAverageExecutionDuration() {
        // Given
        var testCaseResult1 = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
    
        var testCaseResult2 = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                150);
        
        // When
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult1);
                    put("test2", testCaseResult2);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        // Then
        Assertions.assertEquals(125, response.getAverageExecutionDuration());
    }
    
    @Test
    void shouldConvertTimeLimitToMillis() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        // When
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        // Then
        Assertions.assertEquals(15 * 1000, response.getTimeLimit());
    }
    
    @Test
    void givenANotResponseInstanceEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals("not a response instance");
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void shouldReturnTrue() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
    
        var response2 = new Response(
                response.getVerdict(),
                response.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                response.getTimeLimit() / 1000,
                response.getMemoryLimit(),
                response.getLanguage(),
                response.getDateTime());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertTrue(equals);
    }
    
    @Test
    void givenSameInstanceEqualsShouldReturnTrue() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response);
        
        // Then
        Assertions.assertTrue(equals);
    }
    
    @Test
    void equalsShouldNotCompareDateTime() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
    
        var response2 = new Response(
                response.getVerdict(),
                response.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                response.getTimeLimit() / 1000,
                response.getMemoryLimit(),
                response.getLanguage(),
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertTrue(equals);
    }
    
    @Test
    void givenDifferentVerdictsEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        var response2 = new Response(
                Verdict.OUT_OF_MEMORY.getStatusResponse(),
                response.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                response.getTimeLimit() / 1000,
                response.getMemoryLimit(),
                response.getLanguage(),
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void givenDifferentStatusEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        var response2 = new Response(
                response.getVerdict(),
                Verdict.OUT_OF_MEMORY.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                response.getTimeLimit() / 1000,
                response.getMemoryLimit(),
                response.getLanguage(),
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void givenDifferentErrorEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        var response2 = new Response(
                response.getVerdict(),
                response.getStatusCode(),
                "other error",
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                response.getTimeLimit() / 1000,
                response.getMemoryLimit(),
                response.getLanguage(),
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void givenDifferentCompilationDurationEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        var response2 = new Response(
                response.getVerdict(),
                response.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                200,
                response.getTimeLimit() / 1000,
                response.getMemoryLimit(),
                response.getLanguage(),
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void givenDifferentTimeLimitEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        var response2 = new Response(
                response.getVerdict(),
                response.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                10,
                response.getMemoryLimit(),
                response.getLanguage(),
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void givenDifferentMemoryLimitEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        var response2 = new Response(
                response.getVerdict(),
                response.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                response.getTimeLimit() / 1000,
                50,
                response.getLanguage(),
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void givenDifferentLanguageEqualsShouldReturnFalse() {
        // Given
        var testCaseResult = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                100,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        var response2 = new Response(
                response.getVerdict(),
                response.getStatusCode(),
                response.getError(),
                response.getTestCasesResult(),
                response.getCompilationDuration(),
                response.getTimeLimit() / 1000,
                response.getMemoryLimit(),
                Language.C,
                LocalDateTime.now());
        
        // When
        boolean equals = response.equals(response2);
        
        // Then
        Assertions.assertFalse(equals);
    }
}
