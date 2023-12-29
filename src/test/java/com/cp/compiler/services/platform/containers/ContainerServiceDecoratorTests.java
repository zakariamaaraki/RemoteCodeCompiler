package com.cp.compiler.services.platform.containers;

import com.cp.compiler.models.processes.ProcessOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

public class ContainerServiceDecoratorTests {
    
    @Test
    void getRunningContainersShouldReturnSameResult() {
        // Given
        var containerServiceMock = Mockito.mock(ContainerService.class);
        ContainerServiceDecorator containerServiceDecorator = getContainerServiceDecorator(containerServiceMock);
        String expectedOutput = "runningContainers";
        Mockito.when(containerServiceMock.getRunningContainers()).thenReturn(expectedOutput);
        
        // When
        String output = containerServiceDecorator.getRunningContainers();
        
        // Then
        Assertions.assertEquals(expectedOutput, output);
    }
    
    @Test
    void getImagesShouldHaveTheSameBehaviour() {
        // Given
        var containerServiceMock = Mockito.mock(ContainerService.class);
        ContainerServiceDecorator containerServiceDecorator = getContainerServiceDecorator(containerServiceMock);
        String expectedOutput = "images";
        Mockito.when(containerServiceMock.getImages()).thenReturn(expectedOutput);
        
        // When
        String output = containerServiceDecorator.getImages();
        
        // Then
        Assertions.assertEquals(expectedOutput, output);
    }
    
    @Test
    void getContainersStatsShouldHaveTheSameBehaviour() {
        // Given
        var containerServiceMock = Mockito.mock(ContainerService.class);
        ContainerServiceDecorator containerServiceDecorator = getContainerServiceDecorator(containerServiceMock);
        String expectedOutput = "containers stats";
        Mockito.when(containerServiceMock.getContainersStats()).thenReturn(expectedOutput);
        
        // When
        String output = containerServiceDecorator.getContainersStats();
        
        // Then
        Assertions.assertEquals(expectedOutput, output);
    }
    
    @Test
    void getALlContainersStatsShouldHaveTheSameBehaviour() {
        // Given
        var containerServiceMock = Mockito.mock(ContainerService.class);
        ContainerServiceDecorator containerServiceDecorator = getContainerServiceDecorator(containerServiceMock);
        String expectedOutput = "all containers stats";
        Mockito.when(containerServiceMock.getAllContainersStats()).thenReturn(expectedOutput);
        
        // When
        String output = containerServiceDecorator.getAllContainersStats();
        
        // Then
        Assertions.assertEquals(expectedOutput, output);
    }
    
    @Test
    void isUpShouldHaveTheSameBehaviour() {
        // Given
        var containerServiceMock = Mockito.mock(ContainerService.class);
        ContainerServiceDecorator containerServiceDecorator = getContainerServiceDecorator(containerServiceMock);
        Mockito.when(containerServiceMock.isUp()).thenReturn(true);
        
        // When
        boolean output = containerServiceDecorator.isUp();
        
        // Then
        Assertions.assertEquals(true, output);
    }
    
    @Test
    void getContainerizationNameShouldHaveTheSameBehaviour() {
        // Given
        var containerServiceMock = Mockito.mock(ContainerService.class);
        ContainerServiceDecorator containerServiceDecorator = getContainerServiceDecorator(containerServiceMock);
        String expectedOutput = "docker";
        Mockito.when(containerServiceMock.getContainerizationName()).thenReturn(expectedOutput);
        
        // When
        String output = containerServiceDecorator.getContainerizationName();
        
        // Then
        Assertions.assertEquals(expectedOutput, output);
    }
    
    private ContainerServiceDecorator getContainerServiceDecorator(ContainerService containerServiceMock) {
        return new ContainerServiceDecorator(containerServiceMock) {
            @Override
            public String buildContainerImageInternal(String contextPath, String imageName, String dockerfileName) {
                return null;
            }
            
            @Override
            public ProcessOutput runContainerInternal(String imageName,
                                                      String containerName,
                                                      long timeout,
                                                      float maxCpus,
                                                      Map<String, String> envVariables) {
                return null;
            }
            
            @Override
            public ProcessOutput runContainer(String imageName,
                                              String containerName,
                                              long timeout,
                                              String volumeMounting,
                                              String executionPath,
                                              String sourceCodeFileName) {
                return null;
            }
        };
    }
}
