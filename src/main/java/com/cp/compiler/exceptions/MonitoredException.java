package com.cp.compiler.exceptions;

import io.micrometer.core.instrument.Counter;
import lombok.Getter;

/**
 * The type Monitored exception.
 * Used to monitor exceptions
 *
 * @author Zakaria Maaraki
 */
@Getter
public abstract class MonitoredException extends RuntimeException {
    
    private ErrorCode errorCode;
    
    private ErrorType errorType;
    
    private boolean isRetryableError;
    
    private int retryIn; // in Milli-seconds.
    
    /**
     * Instantiates a new Monitored exception and increment the corresponding error counter.
     *
     * @param message          the message
     * @param errorCode        the error code
     * @param errorType        the error type
     * @param isRetryableError the is retryable error
     * @param retryIn          the retry in
     */
    public MonitoredException(String message,
                              ErrorCode errorCode,
                              ErrorType errorType,
                              boolean isRetryableError,
                              int retryIn) {
        super(message);
        
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.isRetryableError = isRetryableError;
        this.retryIn = retryIn;
        
        Counter counter = ErrorCounterFactory.getCounter(this.errorCode);
        if (counter != null) {
            counter.increment();
        }
    }
    
    /**
     * Instantiates a new Monitored exception.
     * Can be used for non retryable errors.
     *
     * @param message   the message
     * @param errorCode the error code
     * @param errorType the error type
     */
    public MonitoredException(String message, ErrorCode errorCode, ErrorType errorType) {
        this(message, errorCode, errorType, false, -1);
    }
    
    /**
     * Gets counter.
     *
     * @return the counter
     */
    public Counter getCounter() {
        return ErrorCounterFactory.getCounter(this.errorCode);
    }
}
