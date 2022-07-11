package com.cp.compiler.healths;

import com.cp.compiler.healthchecks.ContainerHealthIndicator;
import com.cp.compiler.services.ContainerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
class ContainerHealthIndicatorTests {
    
    @Mock
    private ContainerService containerService;
    
    @Test
    void shouldReturnHealthUp() {
        // Given
        var healthIndicator = new ContainerHealthIndicator(containerService);
        Mockito.when(containerService.isUp()).thenReturn(true);
        Mockito.when(containerService.getContainerizationName()).thenReturn("Docker");
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.up().build().getStatus(), health.getStatus());
    }
    
    @Test
    void shouldReturnHealthDown() {
        // Given
        var healthIndicator = new ContainerHealthIndicator(containerService);
        Mockito.when(containerService.isUp()).thenReturn(false);
        Mockito.when(containerService.getContainerizationName()).thenReturn("Docker");
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.down().build().getStatus(), health.getStatus());
    }
}
