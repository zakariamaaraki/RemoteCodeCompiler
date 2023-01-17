package com.cp.compiler.exceptions;

import io.micrometer.core.instrument.Counter;
import lombok.Getter;

/**
 * The type Monitored exception.
 */
@Getter
public class MonitoredException extends RuntimeException {
    
    private ErrorCode errorCode;
    
    private ErrorType errorType;
    
    /**
     * Instantiates a new Monitored exception and increment the corresponding error counter.
     *
     * @param message   the message
     * @param errorCode the error code
     * @param errorType the error type
     */
    public MonitoredException(String message, ErrorCode errorCode, ErrorType errorType) {
        super(message);
        this.errorCode = errorCode;
        this.errorType = errorType;
        ErrorCounterFactory.getCounter(this.errorCode).increment();
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
