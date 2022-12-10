package com.cp.compiler.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class RestConfigTests {
    
    @Autowired
    RestTemplate restTemplate;
    
    @Test
    void restTemplateBeanShouldBeRegistered() {
        Assertions.assertNotNull(restTemplate);
    }
}
