package com.cp.compiler.services.containers;

import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.exceptions.ProcessExecutionException;
import com.cp.compiler.exceptions.ProcessExecutionTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class DefaultContainerServiceTests {
    
    @Test
    void buildImageShouldRetry3TimesIfItFails() throws Exception {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.buildImage(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenThrow(new ContainerFailedDependencyException("Error occurred while building the image"));
    
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerFailedDependencyException.class,
                () -> defaultContainerService.buildImage("test", "test", "test"));
        
        Mockito.verify(containerService, Mockito.times(4))
               .buildImage("test", "test", "test");
    }
    
    @Test
    void runContainerShouldRetry3TimesIfItFailsWithProcessExecutionException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat()))
                .thenThrow(new ProcessExecutionException("Error occurred while running the container"));
        
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerFailedDependencyException.class,
                () -> defaultContainerService.runContainer(
                        "test",
                        "test",
                        2000,
                        0.2f));
        
        Mockito.verify(containerService, Mockito.times(4))
                .runContainer("test", "test", 2000, 0.2f);
    }
    
    @Test
    void runContainerShouldNotRetryIfItFailsWithProcessExecutionTimeoutException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat()))
                .thenThrow(new ProcessExecutionTimeoutException("Timeout occurred while running the container"));
        
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerOperationTimeoutException.class,
                () -> defaultContainerService.runContainer(
                        "test",
                        "test",
                        2000,
                        0.2f));
        
        Mockito.verify(containerService, Mockito.times(1))
                .runContainer("test", "test", 2000, 0.2f);
    }
    
    @Test
    void runContainerWithVolumeShouldRetry3TimesIfItFailsWithProcessExecutionException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenThrow(new ProcessExecutionException("Error occurred while running the container"));
        
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerFailedDependencyException.class,
                () -> defaultContainerService.runContainer(
                        "test",
                        "test",
                        2000,
                        "test",
                        "test",
                        "test"));
        
        Mockito.verify(containerService, Mockito.times(4))
                .runContainer(
                        "test",
                        "test",
                        2000,
                        "test",
                        "test",
                        "test");
    }
    
    @Test
    void runContainerWithVolumeShouldNotRetryIfItFailsWithProcessExecutionTimeoutException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenThrow(new ProcessExecutionTimeoutException("Timeout occurred while running the container"));
        
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerOperationTimeoutException.class,
                () -> defaultContainerService.runContainer(
                        "test",
                        "test",
                        2000,
                        "test",
                        "test",
                        "test"));
        
        Mockito.verify(containerService, Mockito.times(1))
                .runContainer(
                        "test",
                        "test",
                        2000,
                        "test",
                        "test",
                        "test");
    }
    
    @Test
    void shouldThrowContainerFailedDependencyException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat()))
               .thenThrow(new ProcessExecutionException("Error occurred while building the image"));
        
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerFailedDependencyException.class,
                () -> defaultContainerService.runContainer(
                        "does not exists",
                        "does not exists",
                        1,
                        0.2f));
    }
    
    @Test
    void shouldThrowContainerTimeoutException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat()))
                .thenThrow(new ProcessExecutionTimeoutException("Timeout occurred while building the image"));
    
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerOperationTimeoutException.class,
                () -> defaultContainerService.runContainer(
                        "does not exists",
                        "does not exists",
                        1,
                        0.2f));
    }
    
    @Test
    void runContainerWithVolumeShouldThrowContainerTimeoutException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenThrow(new ProcessExecutionTimeoutException("Timeout occurred while building the image"));
    
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerOperationTimeoutException.class,
                () -> defaultContainerService.runContainer(
                        "does not exists",
                        "does not exists",
                        1,
                        "volume",
                        "executionPath",
                        "sourcecode"));
    }
    
    @Test
    void runContainerWithVolumeShouldThrowFailedDependencyException() {
        // Given
        var containerService = Mockito.mock(ContainerService.class);
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenThrow(new ProcessExecutionException("Error occurred while building the image"));
    
        var defaultContainerService = new DefaultContainerService(containerService);
        
        // When / Then
        Assertions.assertThrows(
                ContainerFailedDependencyException.class,
                () -> defaultContainerService.runContainer(
                        "does not exists",
                        "does not exists",
                        1,
                        "volume",
                        "executionPath",
                        "sourcecode"));
    }
}
