package com.cp.compiler.utils.retries;

/**
 * The interface Retryable execution.
 *
 * @param <T> the type parameter
 * @author Zakaria Maaraki
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
