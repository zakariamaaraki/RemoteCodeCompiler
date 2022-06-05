package com.cp.compiler.services;

import com.cp.compiler.repositories.HooksRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest
public class HocksStorageTests {
    
    @Autowired
    private HooksRepository hooksRepository;
    
    @Test
    void shouldAddUrl() {
        // Given
        String imageName = "test";
        String url = "test";
        
        // When
        hooksRepository.addUrl(imageName, url);
        
        // Then
        Assertions.assertTrue(hooksRepository.contains(imageName));
    }
    
    @Test
    void shouldRetrieveUrl() {
        // Given
        String imageName = "test";
        String url = "test";
        hooksRepository.addUrl(imageName, url);
    
        // When
        String retrievedUrl = hooksRepository.get(imageName);
        
        // Then
        Assertions.assertEquals(url, retrievedUrl);
    }
    
    @Test
    void shouldRetrieveAndRemoveUrl() {
        // Given
        String imageName = "test";
        String url = "test";
        hooksRepository.addUrl(imageName, url);
    
        // When
        String retrievedUrl = hooksRepository.getAndRemove(imageName);
    
        // Then
        Assertions.assertFalse(hooksRepository.contains(imageName));
        Assertions.assertEquals(url, retrievedUrl);
    }
    
    @Test
    void shouldReturnTrueIfTheUrlExist() {
        // Given
        String imageName = "test";
        String url = "test";
        hooksRepository.addUrl(imageName, url);
    
        // When
        boolean exists = hooksRepository.contains(imageName);
    
        // Then
        Assertions.assertTrue(exists);
    }
    
    @Test
    void shouldReturnFalseIfTheUrlDoesNotExist() {
        // When
        boolean exists = hooksRepository.contains("test");
    
        // Then
        Assertions.assertFalse(exists);
    }
}
