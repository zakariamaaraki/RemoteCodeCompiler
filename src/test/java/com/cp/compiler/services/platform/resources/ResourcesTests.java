package com.cp.compiler.services.platform.resources;

import com.cp.compiler.contract.resources.AvailableResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ResourcesTests {

    private final float MAX_CPUS = 0.2f;
    private final int MAX_REQUESTS = 1000;
    
    @Test
    void shouldReturnMaxCpus() {
        // Given
        var resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        
        // When
        float returnedMaxCpus = resources.getMaxCpus();
        
        // Then
        Assertions.assertEquals(MAX_CPUS, returnedMaxCpus);
    }
    
    @Test
    void allowNewExecutionShouldReturnTrue() {
        // Given
        Resources resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        
        // When
        boolean allowNewExecution = resources.allowNewExecution();
    
        // Then
        Assertions.assertTrue(allowNewExecution);
    }
    
    @Test
    void reserveResourcesShouldIncrementTheCounter() {
        // Given
        Resources resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        
        // When
        int counter = resources.reserveResources();
        
        // Then
        Assertions.assertEquals(counter, 1);
    }
    
    @Test
    void cleanupShouldDecrementTheCounter() {
        // Given
        Resources resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        resources.reserveResources();
        
        // When
        int counter = resources.cleanup();
        
        // Then
        Assertions.assertEquals(counter, 0);
    }
    
    @Test
    void cleanupShouldReturnZero() {
        // Given
        Resources resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        
        // When
        int counter = resources.cleanup();
        
        // Then
        Assertions.assertEquals(counter, 0);
    }
    
    @Test
    void shouldReturnNumberOfExecutions() {
        // Given
        Resources resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        
        // When
        boolean allow = resources.allowNewExecution();
        resources.reserveResources();
        
        // Then
        Assertions.assertTrue(allow);
        Assertions.assertEquals(1, resources.getNumberOfExecutions());
    }
    
    @Test
    void shouldReturnMaxNumberOfRequests() {
        // Given
        Resources resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        
        // When
        int maxRequests = resources.getMaxRequests();
        
        // Then
        Assertions.assertEquals(maxRequests, resources.getMaxRequests());
    }
    
    @Test
    void getAvailableResourcesShouldReturnTheRightValue() {
        // Given
        Resources resources = new ResourcesDefault(MAX_CPUS, MAX_REQUESTS);
        
        // When
        AvailableResources availableResources = resources.getAvailableResources();
        
        // Then
        Assertions.assertEquals(
                Runtime.getRuntime().availableProcessors() - (resources.getNumberOfExecutions() * MAX_CPUS)
                ,availableResources.getAvailableCpus());
        Assertions.assertEquals(resources.getNumberOfExecutions(), availableResources.getCurrentExecutions());
        Assertions.assertEquals(resources.getMaxRequests(), availableResources.getMaxNumberOfExecutions());
    }
}
