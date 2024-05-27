package com.cp.compiler.services.platform.ratelimiting;

public interface UserBucketService {
    LeakyBucket getBucket(String userId);
}
