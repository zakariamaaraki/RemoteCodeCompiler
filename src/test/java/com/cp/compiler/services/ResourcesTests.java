package com.cp.compiler.services;

import com.cp.compiler.models.AvailableResources;
import com.cp.compiler.services.resources.Resources;
import com.cp.compiler.services.resources.ResourcesDefault;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.atomic.AtomicInteger;

public class ResourcesTests {

    private Resources resources = new ResourcesDefault(8, 1000);
    
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
    
    @Test
    void shouldReturnNumberOfExecutions() {
        // When
        boolean allow = resources.allowNewExecution();
        resources.reserveResources();
        
        // Then
        Assertions.assertTrue(allow);
        Assertions.assertEquals(1, resources.getNumberOfExecutions());
    }
    
    @Test
    void shouldReturnMaxNumberOfRequests() {
        // When
        int maxRequests = resources.getMaxRequests();
        
        // Then
        Assertions.assertEquals(maxRequests, resources.getMaxRequests());
    }
    
    @Test
    void getAvailableResourcesShouldReturnTheRightValue() {
        // When
        AvailableResources availableResources = resources.getAvailableResources();
        
        // Then
        Assertions.assertEquals(8, availableResources.getAvailableCpus());
        Assertions.assertEquals(resources.getNumberOfExecutions(), availableResources.getCurrentExecutions());
        Assertions.assertEquals(resources.getMaxRequests(), availableResources.getMaxNumberOfExecutions());
    }
}
