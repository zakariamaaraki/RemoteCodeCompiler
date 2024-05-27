package com.cp.compiler.services.platform.ratelimiting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class UserBucketServiceDefaultTest {

    private UserBucketService userBucketService;
    private static final int CAPACITY = 5;
    private static final int LEAK_RATE = 1000;
    private static final int MAX_SIZE = 3;

    @BeforeEach
    void setUp() {
        userBucketService = new UserBucketServiceDefault(CAPACITY, LEAK_RATE, MAX_SIZE);
    }

    @Test
    void testGetBucket_NewBucket() {
        LeakyBucket bucket1 = userBucketService.getBucket("user1");

        assertNotNull(bucket1);
        assertEquals(true, bucket1.allowRequest());
    }

    @Test
    void testGetBucket_ExistingBucket() {
        LeakyBucket bucket1 = userBucketService.getBucket("user1");
        LeakyBucket bucket2 = userBucketService.getBucket("user1");

        assertSame(bucket1, bucket2);
    }
}