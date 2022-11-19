package com.cp.compiler.utils.retries;

/**
 * The interface Retryable execution.
 *
 * @param <T> the type parameter
 */
@FunctionalInterface
public interface RetryableExecution<T> {
    
    /**
     * Execute t.
     *
     * @return the t
     */
    T execute() throws Exception;
}
