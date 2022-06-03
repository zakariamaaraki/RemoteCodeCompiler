package com.cp.compiler.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@SpringBootTest
public class HocksStorageTests {
    
    @Autowired
    private HooksStorage hooksStorage;
    
    @Test
    void shouldAddUrl() {
        // Given
        String imageName = "test";
        String url = "test";
        
        // When
        hooksStorage.addUrl(imageName, url);
        
        // Then
        Assertions.assertTrue(hooksStorage.contains(imageName));
    }
    
    @Test
    void shouldRetrieveUrl() {
        // Given
        String imageName = "test";
        String url = "test";
        hooksStorage.addUrl(imageName, url);
    
        // When
        String retrievedUrl = hooksStorage.get(imageName);
        
        // Then
        Assertions.assertEquals(url, retrievedUrl);
    }
    
    @Test
    void shouldRetrieveAndRemoveUrl() {
        // Given
        String imageName = "test";
        String url = "test";
        hooksStorage.addUrl(imageName, url);
    
        // When
        String retrievedUrl = hooksStorage.getAndRemove(imageName);
    
        // Then
        Assertions.assertFalse(hooksStorage.contains(imageName));
        Assertions.assertEquals(url, retrievedUrl);
    }
    
    @Test
    void shouldReturnTrueIfTheUrlExist() {
        // Given
        String imageName = "test";
        String url = "test";
        hooksStorage.addUrl(imageName, url);
    
        // When
        boolean exists = hooksStorage.contains(imageName);
    
        // Then
        Assertions.assertTrue(exists);
    }
    
    @Test
    void shouldReturnFalseIfTheUrlDoesNotExist() {
        // When
        boolean exists = hooksStorage.contains("test");
    
        // Then
        Assertions.assertFalse(exists);
    }
}
