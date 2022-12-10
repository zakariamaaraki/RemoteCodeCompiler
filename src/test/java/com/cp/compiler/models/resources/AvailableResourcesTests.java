package com.cp.compiler.models.resources;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AvailableResourcesTests {
    
    @Test
    void shouldReturnTrue() {
        // Given
        var availableResources = new AvailableResources(0.2f, 1000, 1);
        var availableResources2 = new AvailableResources(0.2f, 1000, 1);
        
        // When
        boolean equals = availableResources.equals(availableResources2);
        
        // Then
        Assertions.assertTrue(equals);
    }
    
    @Test
    void shouldReturnFalseIfTypeIsDifferent() {
        // Given
        var availableResources = new AvailableResources(0.2f, 1000, 1);
        
        // When
        boolean equals = availableResources.equals("other type");
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void shouldReturnFalseIfAvailableCpusAreDifferent() {
        // Given
        var availableResources = new AvailableResources(0.2f, 1000, 1);
        var availableResources2 = new AvailableResources(0.3f, 1000, 1);
        
        // When
        boolean equals = availableResources.equals(availableResources2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void shouldReturnFalseIfMaxNumberOfExecutionsAreDifferent() {
        // Given
        var availableResources = new AvailableResources(0.2f, 1000, 1);
        var availableResources2 = new AvailableResources(0.2f, 1001, 1);
        
        // When
        boolean equals = availableResources.equals(availableResources2);
        
        // Then
        Assertions.assertFalse(equals);
    }
    
    @Test
    void shouldReturnFalseIfCurrentExecutionsAreDifferent() {
        // Given
        var availableResources = new AvailableResources(0.2f, 1000, 1);
        var availableResources2 = new AvailableResources(0.2f, 1000, 2);
        
        // When
        boolean equals = availableResources.equals(availableResources2);
        
        // Then
        Assertions.assertFalse(equals);
    }
}
