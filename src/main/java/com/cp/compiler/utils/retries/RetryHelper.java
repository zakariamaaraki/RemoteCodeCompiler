package com.cp.compiler.utils.retries;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The type Retry helper.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public abstract class RetryHelper {
    
    private RetryHelper() {}
    
    /**
     * Execute with retry a function.
     *
     * @param <T>                      the type parameter
     * @param execution                the function that should be executed
     * @param exceptionsAllowList      set of exceptions which are not retryable (give *.class.getName())
     * @param maxRetries               the max number of retries
     * @param durationBetweenEachRetry the duration between each retry in msec
     * @return the t
     * @throws exception               the exception
     */
    public static <T> T executeWithRetries(RetryableExecution<T> execution,
                                           Set<String> exceptionsAllowList,
                                           int maxRetries,
                                           int durationBetweenEachRetry) throws Exception {
        
        if (maxRetries < 0) {
            throw new IllegalArgumentException("maxRetries should be a positive value");
        }
        
        if (durationBetweenEachRetry < 0) {
            throw new IllegalArgumentException("durationBetweenEachRetry should be a positive value");
        }
        
        try {
            return execution.execute();
        } catch (Exception exception) {
            if (exceptionsAllowList != null && exceptionsAllowList.contains(exception.getClass().getName())) {
                // Should not retry
                throw exception;
            }
            log.debug("No exception found in the allow set of exceptions");
            if (maxRetries > 0) {
                log.error("Error: {}, will try again after {}", exception, durationBetweenEachRetry);
                TimeUnit.MILLISECONDS.sleep(durationBetweenEachRetry);
                return executeWithRetries(execution, exceptionsAllowList, maxRetries - 1, durationBetweenEachRetry);
            } else {
                log.error("Error: {}, max number of retries reached!", exception);
                throw exception;
            }
        }
    }
}
