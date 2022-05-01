package com.cp.compiler.services;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Hooks storage.
 */
@Component
public class HooksStorageImpl implements HooksStorage {
    
    private final Map<String, String> hooks = new HashMap<>();
    
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
