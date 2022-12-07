package com.cp.compiler.models.testcases;

import com.cp.compiler.models.Verdict;
import com.cp.compiler.models.testcases.TestCaseResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCaseResultTests {

    @Test
    void shouldReturnTrue() {
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
                100);
        
        // When
        boolean equals = testCaseResult1.equals(testCaseResult2);
        
        // Then
        Assertions.assertTrue(equals);
    }
    
    @Test
    void ifVerdictIsDifferentShouldReturnFalse() {
        // Given
        var testCaseResult1 = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var testCaseResult2 = new TestCaseResult(
                Verdict.WRONG_ANSWER,
                "output",
                "error",
                "expectedOutput",
                100);
        
        // When
        boolean equals = testCaseResult1.equals(testCaseResult2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void ifOutputIsDifferentShouldReturnFalse() {
        // Given
        var testCaseResult1 = new TestCaseResult(
                Verdict.ACCEPTED,
                "output",
                "error",
                "expectedOutput",
                100);
        
        var testCaseResult2 = new TestCaseResult(
                Verdict.ACCEPTED,
                "output2",
                "error",
                "expectedOutput",
                100);
        
        // When
        boolean equals = testCaseResult1.equals(testCaseResult2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void ifErrorIsDifferentShouldReturnFalse() {
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
                "error2",
                "expectedOutput",
                100);
        
        // When
        boolean equals = testCaseResult1.equals(testCaseResult2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void ifExpectedOutputIsDifferentShouldReturnFalse() {
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
                "expectedOutput2",
                100);
        
        // When
        boolean equals = testCaseResult1.equals(testCaseResult2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void ifExecutionDurationIsDifferentShouldReturnFalse() {
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
                90);
        
        // When
        boolean equals = testCaseResult1.equals(testCaseResult2);
        
        // Then
        Assertions.assertFalse(equals);
    }
}
