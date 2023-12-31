package com.cp.compiler.services.platform.containers;

import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ProcessExecutionTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class DockerServiceTests {
    
    @Autowired
    private DockerContainerService containerService;
    
    @Test
    void shouldThrowContainerFailedDependencyExecutionException() {
        // When / Then
        Assertions.assertThrows(
                ContainerFailedDependencyException.class,
                () -> containerService.buildImage(
                        "test",
                        "does not exists",
                        "test",
                        false));
    }
    
    @Test
    void shouldThrowProcessTimeoutException() {
        // When / Then
        Assertions.assertThrows(
                ProcessExecutionTimeoutException.class,
                () -> containerService.runContainer(
                        "does not exists",
                        "does not exists",
                        1,
                        0.2f,
                        new HashMap<>()));
    }
    
    @Test
    void runContainerWithVolumeShouldThrowProcessTimeoutException() {
        // When / Then
        Assertions.assertThrows(
                ProcessExecutionTimeoutException.class,
                () -> containerService.runContainer(
                        "does not exists",
                        "does not exists",
                        1,
                        "volume",
                        "executionPath",
                        "sourcecode"));
    }
}
