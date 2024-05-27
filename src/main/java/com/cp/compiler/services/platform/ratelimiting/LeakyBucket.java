package com.cp.compiler.services.platform.ratelimiting;

public class LeakyBucket {

    private final int capacity;
    private final int leakRate;
    private int queueSize;

    private long lastLeakTime;

    public LeakyBucket(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.queueSize = 0;
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        leak();
        if (queueSize < capacity) {
            queueSize++;
            return true;
        } else {
            return false;
        }
    }

    private void leak() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastLeakTime;
        int leaks = (int) (timeElapsed / leakRate);

        if (leaks > 0) {
            queueSize = Math.max(0, queueSize - leaks);
            lastLeakTime = currentTime;
        }
    }

}
