package com.cp.compiler.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest
public class ResourcesTests {
    
    @Autowired
    private Resources resources;
    
    @Test
    void shouldReturnMaxCpus() {
        // Given
        int maxCpus = Runtime.getRuntime().availableProcessors();
        
        // When
        float returnedMaxCpus = resources.getMaxCpus();
        
        // Then
        Assertions.assertEquals((int)returnedMaxCpus, maxCpus);
    }
    
    @Test
    void allowNewExecutionShouldReturnTrue() {
        // When
        boolean allowNewExecution = resources.allowNewExecution();
    
        // Then
        Assertions.assertTrue(allowNewExecution);
    }
    
    @Test
    void reserveResourcesShouldIncrementTheCounter() {
        // When
        int counter = resources.reserveResources();
        
        // Then
        Assertions.assertEquals(counter, 1);
    }
    
    @Test
    void cleanupShouldDecrementTheCounter() {
        // Given
        resources.reserveResources();
        
        // When
        int counter = resources.cleanup();
        
        // Then
        Assertions.assertEquals(counter, 0);
    }
    
    @Test
    void cleanupShouldReturn0() {
        // When
        int counter = resources.cleanup();
        
        // Then
        Assertions.assertEquals(counter, 0);
    }
}
