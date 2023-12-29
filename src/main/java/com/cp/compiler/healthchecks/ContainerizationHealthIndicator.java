package com.cp.compiler.healthchecks;

import com.cp.compiler.services.platform.containers.ContainerService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * The type Container health indicator.
 *
 * @author Zakaria Maaraki
 */
@Component
public class ContainerizationHealthIndicator implements HealthIndicator {
    
    private final ContainerService containerService;
    
    private static final String BROKEN_STATE = "Container Down";
    
    private static final String UP_STATE = "Containerization UP";
    
    /**
     * Instantiates a new Container health indicator.
     *
     * @param containerService the container service
     */
    public ContainerizationHealthIndicator(ContainerService containerService) {
        super();
        this.containerService = containerService;
    }
    
    @Override
    public Health health() {
        if (containerService.isUp()) {
            return Health.up()
                    .withDetail("Containerization", containerService.getContainerizationName())
                    .withDetail("State", UP_STATE)
                    .build();
        }
        return Health.down()
                .withDetail("Containerization", containerService.getContainerizationName())
                .withDetail("State", BROKEN_STATE)
                .build();
    }
}
