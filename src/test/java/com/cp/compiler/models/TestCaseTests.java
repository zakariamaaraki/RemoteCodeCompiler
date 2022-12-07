package com.cp.compiler.models;

import com.cp.compiler.models.testcases.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCaseTests {
    
    @Test
    void equalsShouldReturnTrue() {
        // Given
        TestCase testCase1 = new TestCase("input", "output");
        TestCase testCase2 = new TestCase("input", "output");
        
        // When
        boolean equals = testCase1.equals(testCase2);
        
        // Then
        Assertions.assertTrue(equals);
    }
    
    @Test
    void whenInputIsDifferentEqualsShouldReturnFalse() {
        // Given
        TestCase testCase1 = new TestCase("input", "output");
        TestCase testCase2 = new TestCase("input2", "output");
        
        // When
        boolean equals = testCase1.equals(testCase2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void whenExpectedOutputIsDifferentEqualsShouldReturnFalse() {
        // Given
        TestCase testCase1 = new TestCase("input", "output");
        TestCase testCase2 = new TestCase("input", "output2");
        
        // When
        boolean equals = testCase1.equals(testCase2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void whenInputAndExpectedOutputAreDifferentEqualsShouldReturnFalse() {
        // Given
        TestCase testCase1 = new TestCase("input", "output");
        TestCase testCase2 = new TestCase("input2", "output2");
        
        // When
        boolean equals = testCase1.equals(testCase2);
        
        // Then
        Assertions.assertFalse(equals);
    }
}
