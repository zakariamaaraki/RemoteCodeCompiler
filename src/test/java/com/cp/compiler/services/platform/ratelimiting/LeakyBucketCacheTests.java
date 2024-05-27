package com.cp.compiler.services.platform.ratelimiting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class LeakyBucketCacheTest {

    private LeakyBucketCache leakyBucketCache;
    private static final int MAX_SIZE = 3;

    @BeforeEach
    void setUp() {
        leakyBucketCache = new LeakyBucketCache(MAX_SIZE);
    }

    @Test
    void testAddElements() {
        LeakyBucket bucket1 = new LeakyBucket(5, 1000);
        LeakyBucket bucket2 = new LeakyBucket(5, 1000);
        LeakyBucket bucket3 = new LeakyBucket(5, 1000);

        leakyBucketCache.put("user1", bucket1);
        leakyBucketCache.put("user2", bucket2);
        leakyBucketCache.put("user3", bucket3);

        assertEquals(3, leakyBucketCache.size());
        assertTrue(leakyBucketCache.containsKey("user1"));
        assertTrue(leakyBucketCache.containsKey("user2"));
        assertTrue(leakyBucketCache.containsKey("user3"));
    }

    @Test
    void testRemoveEldestEntry() {
        LeakyBucket bucket1 = new LeakyBucket(5, 1000);
        LeakyBucket bucket2 = new LeakyBucket(5, 1000);
        LeakyBucket bucket3 = new LeakyBucket(5, 1000);
        LeakyBucket bucket4 = new LeakyBucket(5, 1000);

        leakyBucketCache.put("user1", bucket1);
        leakyBucketCache.put("user2", bucket2);
        leakyBucketCache.put("user3", bucket3);
        leakyBucketCache.put("user4", bucket4);

        assertEquals(3, leakyBucketCache.size());
        assertFalse(leakyBucketCache.containsKey("user1"));
        assertTrue(leakyBucketCache.containsKey("user2"));
        assertTrue(leakyBucketCache.containsKey("user3"));
        assertTrue(leakyBucketCache.containsKey("user4"));
    }

    @Test
    void testAccessOrder() {
        LeakyBucket bucket1 = new LeakyBucket(5, 1000);
        LeakyBucket bucket2 = new LeakyBucket(5, 1000);
        LeakyBucket bucket3 = new LeakyBucket(5, 1000);
        LeakyBucket bucket4 = new LeakyBucket(5, 1000);

        leakyBucketCache.put("user1", bucket1);
        leakyBucketCache.put("user2", bucket2);
        leakyBucketCache.put("user3", bucket3);

        // Access "user1" to make it recently used
        leakyBucketCache.get("user1");

        leakyBucketCache.put("user4", bucket4);

        assertEquals(3, leakyBucketCache.size());
        assertTrue(leakyBucketCache.containsKey("user1"));
        assertFalse(leakyBucketCache.containsKey("user2"));
        assertTrue(leakyBucketCache.containsKey("user3"));
        assertTrue(leakyBucketCache.containsKey("user4"));
    }

    @Test
    void testCapacityMaintained() {
        for (int i = 1; i <= 10; i++) {
            leakyBucketCache.put("user" + i, new LeakyBucket(5, 1000));
        }

        assertEquals(MAX_SIZE, leakyBucketCache.size());
        assertFalse(leakyBucketCache.containsKey("user1"));
        assertFalse(leakyBucketCache.containsKey("user2"));
        assertFalse(leakyBucketCache.containsKey("user7"));
        assertTrue(leakyBucketCache.containsKey("user8"));
        assertTrue(leakyBucketCache.containsKey("user9"));
        assertTrue(leakyBucketCache.containsKey("user10"));
    }
}
