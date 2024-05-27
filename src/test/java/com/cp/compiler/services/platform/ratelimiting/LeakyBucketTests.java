package com.cp.compiler.services.platform.ratelimiting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class LeakyBucketTests {

    private LeakyBucket leakyBucket;

    @BeforeEach
    void setUp() {
        leakyBucket = new LeakyBucket(5, 1000); // capacity of 5, leak rate of 1 request per second
    }

    @Test
    void testAllowRequestWhenNotFull() {
        assertTrue(leakyBucket.allowRequest());
        assertTrue(leakyBucket.allowRequest());
        assertTrue(leakyBucket.allowRequest());
        assertTrue(leakyBucket.allowRequest());
        assertTrue(leakyBucket.allowRequest());
    }

    @Test
    void testRejectRequestWhenFull() {
        // Fill the bucket
        for (int i = 0; i < 5; i++) {
            leakyBucket.allowRequest();
        }
        // Next request should be rejected
        assertFalse(leakyBucket.allowRequest());
    }

    @Test
    void testLeakOverTime() throws InterruptedException {
        // Fill the bucket
        for (int i = 0; i < 5; i++) {
            leakyBucket.allowRequest();
        }
        // Wait for enough time to leak one request
        Thread.sleep(1000);
        assertTrue(leakyBucket.allowRequest());
    }

    @Test
    void testMultipleLeaksOverTime() throws InterruptedException {
        // Fill the bucket
        for (int i = 0; i < 5; i++) {
            leakyBucket.allowRequest();
        }
        // Wait for enough time to leak three requests
        Thread.sleep(3000);
        assertTrue(leakyBucket.allowRequest());
        assertTrue(leakyBucket.allowRequest());
        assertTrue(leakyBucket.allowRequest());
    }

    @Test
    void testImmediateRequestsAfterLeak() throws InterruptedException {
        // Fill the bucket
        for (int i = 0; i < 5; i++) {
            leakyBucket.allowRequest();
        }
        // Wait for enough time to leak one request
        Thread.sleep(1000);
        // Allow one more request
        assertTrue(leakyBucket.allowRequest());
        // Next request should be rejected as the bucket is full again
        assertFalse(leakyBucket.allowRequest());
    }
}
