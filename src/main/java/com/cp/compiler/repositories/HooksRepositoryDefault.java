package com.cp.compiler.repositories;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Hooks repository.
 */
@Repository
public class HooksRepositoryDefault implements HooksRepository {
    
    // Full concurrency of retrievals
    private final Map<String, String> hooks = new ConcurrentHashMap<>();
    
    @Override
    public String get(String executionId) {
        return hooks.get(executionId);
    }
    
    @Override
    public String getAndRemove(String executionId) {
        return hooks.remove(executionId);
    }
    
    @Override
    public boolean contains(String executionId) {
        return hooks.containsKey(executionId);
    }
    
    @Override
    public void addUrl(String executionId, String url) {
        hooks.put(executionId, url);
    }
}
