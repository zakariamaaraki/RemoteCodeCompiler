package com.cp.compiler.services;

/**
 * The interface Cpu resources.
 */
public interface Resources {
    
    /**
     * Gets max cpus.
     *
     * @return the max cpus
     */
    float getMaxCpus();
    
    /**
     * Allow new execution boolean.
     *
     * @return the boolean
     */
    boolean allowNewExecution();
    
    /**
     * Increment number of executions.
     *
     * @return the number of current executions
     */
    int reserveResources();
    
    /**
     * Decrement number of executions.
     *
     * @return the number of current executions
     */
    int cleanup();
    
    /**
     * Number of executions int.
     *
     * @return the number of current executions
     */
    int getNumberOfExecutions();
    
    /**
     * Max requests int.
     *
     * @return max parallel allowed requests
     */
    int getMaxRequests();
}
