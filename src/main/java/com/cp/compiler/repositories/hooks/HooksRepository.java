package com.cp.compiler.repositories.hooks;

/**
 * The interface Hooks repository.
 * This class is used to store urls used for long-running executions.
 *
 * @author Zakaria Maaraki
 */
public interface HooksRepository {
    
    /**
     * Get string.
     *
     * @param executionId the execution id
     * @return the string
     */
    String get(String executionId);
    
    /**
     * Get and remove hook.
     *
     * @param executionId the execution id
     * @return the url
     */
    String getAndRemove(String executionId);
    
    /**
     * Check if it contains a url.
     *
     * @param executionId the execution id
     * @return the boolean
     */
    boolean contains(String executionId);
    
    /**
     * Add url.
     *
     * @param executionId the execution id
     */
    void addUrl(String executionId, String url);
}
