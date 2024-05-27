package com.cp.compiler.services.platform.ratelimiting;

import java.util.Collections;
import java.util.Map;

public class UserBucketServiceDefault implements UserBucketService {

    private final Map<String, LeakyBucket> buckets;
    private final int capacity;
    private final int leakRate;

    public UserBucketServiceDefault(int capacity, int leakRate, int maxSize) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.buckets = Collections.synchronizedMap(new LeakyBucketCache(maxSize));
    }

    public LeakyBucket getBucket(String userId) {
        return buckets.computeIfAbsent(userId, k -> new LeakyBucket(capacity, leakRate));
    }
}
