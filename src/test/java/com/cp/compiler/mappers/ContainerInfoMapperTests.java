package com.cp.compiler.mappers;

import com.cp.compiler.models.containers.ContainerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContainerInfoMapperTests {
    
    @Test
    void shouldConvertJsonToContainerInfo() throws JsonProcessingException {
        // Given
        String jsonContainerInfo = "{\"status\": \"Exited\", \"error\": \"***\", \"exitCode\": 0}";
        
        // When
        ContainerInfo containerInfo = ContainerInfoMapper.toContainerInfo(jsonContainerInfo);
        
        var expectedContainerInfo = new ContainerInfo();
        expectedContainerInfo.setStatus("Exited");
        expectedContainerInfo.setError("***");
        expectedContainerInfo.setExitCode(0);
        
        // Then
        Assertions.assertNotNull(containerInfo);
        Assertions.assertEquals(expectedContainerInfo, containerInfo);
    }
    
    @Test
    void shouldThrowJsonProcessingException() {
        // Given
        String jsonContainerInfo = "{notValid}";
        
        // When / Then
        Assertions.assertThrows(JsonProcessingException.class, () -> {
            ContainerInfoMapper.toContainerInfo(jsonContainerInfo);
        });
    }
}
