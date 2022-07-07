package com.cp.compiler.utils;

import com.cp.compiler.models.Verdict;

/**
 * The type Status util.
 *
 * @author Zakaria Maaraki
 */
public abstract class StatusUtils {
    
    public static final int ACCEPTED_OR_WRONG_ANSWER_STATUS = 0;
    
    public static final int COMPILATION_ERROR_STATUS = 96; // random number
    
    public static final int OUT_OF_MEMORY_STATUS = 139;
    
    public static final int TIME_LIMIT_EXCEEDED_STATUS = 124;
    
    private StatusUtils() {
    }
    
    /**
     * Status response string.
     *
     * @param status an integer that represents the status returned by the docker container
     * @param ans    if the status code is 0, then this boolean must be equal to true or false to specify if Response is Accepted, or it's a Wrong answer
     * @return return a Verdict representing the status response
     */
    public static Verdict statusResponse(int status, boolean ans) {
        
        // the status is taken from the return code of the container
        switch (status) {
            
            // The code compile and the execution finished before the time limit, and the memory does not exceed the limit
            case ACCEPTED_OR_WRONG_ANSWER_STATUS:
                // Is it the excepted output ?
                if (ans)
                    return Verdict.ACCEPTED;
                else
                    return Verdict.WRONG_ANSWER;
            
            case COMPILATION_ERROR_STATUS: return Verdict.COMPILATION_ERROR;
            
            case OUT_OF_MEMORY_STATUS: return Verdict.OUT_OF_MEMORY;
            
            case TIME_LIMIT_EXCEEDED_STATUS: return Verdict.TIME_LIMIT_EXCEEDED;
            
            default: return Verdict.RUNTIME_ERROR;
        }
    }
}
