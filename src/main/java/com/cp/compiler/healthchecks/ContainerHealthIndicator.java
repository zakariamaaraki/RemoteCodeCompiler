package com.cp.compiler.healthchecks;

import com.cp.compiler.services.ContainerService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ContainerHealthIndicator implements HealthIndicator {
    
    private ContainerService containerService;
    
    public ContainerHealthIndicator(ContainerService containerService) {
        super();
        this.containerService = containerService;
    }
    
    @Override
    public Health health() {
        if (containerService.isUp()) {
            return Health.up().build();
        }
        return Health.down().withDetail("state", "Container Down").build();
    }
}
