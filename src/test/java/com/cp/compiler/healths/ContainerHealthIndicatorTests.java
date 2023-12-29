package com.cp.compiler.healths;

import com.cp.compiler.healthchecks.ContainerizationHealthIndicator;
import com.cp.compiler.services.platform.containers.ContainerService;
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
        var healthIndicator = new ContainerizationHealthIndicator(containerService);
        Mockito.when(containerService.isUp()).thenReturn(true);
        Mockito.when(containerService.getContainerizationName()).thenReturn("Docker");
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.up().build().getStatus(), health.getStatus());
        Assertions.assertNotNull(health.getDetails());
    }
    
    @Test
    void shouldReturnHealthDown() {
        // Given
        var healthIndicator = new ContainerizationHealthIndicator(containerService);
        Mockito.when(containerService.isUp()).thenReturn(false);
        Mockito.when(containerService.getContainerizationName()).thenReturn("Docker");
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.down().build().getStatus(), health.getStatus());
        Assertions.assertNotNull(health.getDetails());
    }
}
