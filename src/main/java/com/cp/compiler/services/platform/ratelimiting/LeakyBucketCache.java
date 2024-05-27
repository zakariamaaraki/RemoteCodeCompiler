package com.cp.compiler.services.platform.ratelimiting;

import java.util.LinkedHashMap;
import java.util.Map;

public class LeakyBucketCache extends LinkedHashMap<String, LeakyBucket> {
    private final int maxSize;

    public LeakyBucketCache(int maxSize) {
        super(maxSize, 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, LeakyBucket> eldest) {
        return size() > maxSize;
    }
}