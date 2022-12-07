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
}
