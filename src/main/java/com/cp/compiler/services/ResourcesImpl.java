package com.cp.compiler.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ResourcesImpl implements Resources {
    
    @Getter
    @Value("${compiler.execution.max-cpus}")
    private Float maxCpus;
    
    private AtomicInteger executionsCounter = new AtomicInteger(0);
    
    @Getter
    @Value("${compiler.max-requests}")
    private int maxRequests;
    
    @Override
    public float getMaxCpus() {
        return maxCpus == 0 ? Runtime.getRuntime().availableProcessors() : maxCpus;
    }
    
    @Override
    public boolean allowNewExecution() {
        return executionsCounter.get() < maxRequests && cpuIsAvailable();
    }
    
    private boolean cpuIsAvailable() {
        return maxCpus == 0 || maxCpus * executionsCounter.get() < Runtime.getRuntime().availableProcessors();
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
}
