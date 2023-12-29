package com.cp.compiler.services.platform.resources;

import com.cp.compiler.contract.resources.AvailableResources;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Resources default.
 *
 * @author Zakaria Maaraki
 */
@Component
public class ResourcesDefault implements Resources {
    

    private final Float maxCpus;
    
    private AtomicInteger executionsCounter = new AtomicInteger(0);
    
    @Getter
    private final int maxRequests;
    
    /**
     * Instantiates a new Resources default.
     *
     * @param maxCpus     the max cpus
     * @param maxRequests the max requests
     */
    public ResourcesDefault(@Value("${compiler.execution.max-cpus}")float maxCpus,
                            @Value("${compiler.max-requests}")int maxRequests) {
        this.maxCpus = maxCpus;
        this.maxRequests = maxRequests;
    }
    
    @Override
    public float getMaxCpus() {
        return maxCpus == 0f ? getSystemCpus() : maxCpus;
    }
    
    @Override
    public boolean allowNewExecution() {
        return executionsCounter.get() < maxRequests && cpuIsAvailable();
    }
    
    private boolean cpuIsAvailable() {
        return maxCpus == 0f || maxCpus * executionsCounter.get() < getSystemCpus();
    }
    
    @Override
    public int reserveResources() {
        return executionsCounter.incrementAndGet();
    }
    
    @Override
    public int cleanup() {
        if (executionsCounter.get() == 0) {
            return 0;
        }
        return executionsCounter.decrementAndGet();
    }
    
    @Override
    public int getNumberOfExecutions() {
        return executionsCounter.get();
    }
    
    @Override
    public AvailableResources getAvailableResources() {
        
        int numberOfExecutions = getNumberOfExecutions();
        float availableCpus = getSystemCpus() - (numberOfExecutions * maxCpus);
        
        return AvailableResources
                .builder()
                .availableCpus(availableCpus)
                .maxNumberOfExecutions(getMaxRequests())
                .currentExecutions(numberOfExecutions)
                .build();
    }
    
    private float getSystemCpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
