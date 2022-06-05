package com.cp.compiler.repositories;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Hooks repository.
 */
@Repository
public class HooksRepositoryImpl implements HooksRepository {
    
    // Full concurrency of retrievals
    private final Map<String, String> hooks = new ConcurrentHashMap<>();
    
    @Override
    public String get(String imageName) {
        return hooks.get(imageName);
    }
    
    @Override
    public String getAndRemove(String imageName) {
        return hooks.remove(imageName);
    }
    
    @Override
    public boolean contains(String imageName) {
        return hooks.containsKey(imageName);
    }
    
    @Override
    public void addUrl(String imageName, String url) {
        hooks.put(imageName, url);
    }
}
