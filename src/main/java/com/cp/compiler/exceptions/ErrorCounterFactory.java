package com.cp.compiler.exceptions;

import io.micrometer.core.instrument.Counter;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Error counter factory.
 * This factory contains all registered counters used for monitoring purpose.
 *
 * @author Zakaria Maaraki
 */
public abstract class ErrorCounterFactory {
    
    private static Map<ErrorCode, Counter> counters = new HashMap<>();
    
    /**
     * Register counter.
     *
     * @param errorCode the error code
     * @param counter   the counter
     */
    public static void registerCounter(ErrorCode errorCode, Counter counter) {
        counters.put(errorCode, counter);
    }
    
    /**
     * Gets counter.
     *
     * @param errorCode the error code
     * @return the counter
     */
    public static Counter getCounter(ErrorCode errorCode) {
        return counters.get(errorCode);
    }
}
