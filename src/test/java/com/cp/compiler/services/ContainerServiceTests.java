package com.cp.compiler.services;

import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ContainerServiceTests {
    
    @Autowired
    private ContainerServiceDefault containerService;
    
    @Test
    void shouldThrowContainerFailedDependencyException() {
        // Then
        Assertions.assertThrows(
                ContainerFailedDependencyException.class,
                () -> containerService.buildImage("test", "does not exists"));
    }
    
    @Test
    void shouldThrowContainerTimeoutException() {
        // Then
        Assertions.assertThrows(
                ContainerOperationTimeoutException.class,
                () -> containerService.runContainer("does not exists", 1));
    }
}
