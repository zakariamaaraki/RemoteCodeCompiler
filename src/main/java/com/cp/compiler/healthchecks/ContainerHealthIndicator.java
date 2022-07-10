package com.cp.compiler.healthchecks;

import com.cp.compiler.services.ContainerService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * The type Container health indicator.
 */
@Component
public class ContainerHealthIndicator implements HealthIndicator {
    
    private ContainerService containerService;
    
    /**
     * Instantiates a new Container health indicator.
     *
     * @param containerService the container service
     */
    public ContainerHealthIndicator(ContainerService containerService) {
        super();
        this.containerService = containerService;
    }
    
    @Override
    public Health health() {
        if (containerService.isUp()) {
            return Health.up()
                    .withDetail("Containerization", containerService.getContainerizationName())
                    .build();
        }
        return Health.down()
                .withDetail("Containerization", containerService.getContainerizationName())
                .withDetail("State", "Container Down")
                .build();
    }
}
