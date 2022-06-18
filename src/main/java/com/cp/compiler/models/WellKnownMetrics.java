package com.cp.compiler.models;

/**
 * The type Well known metrics.
 */
public abstract class WellKnownMetrics {
    
    private WellKnownMetrics() {}
    
    /**
     * The constant JAVA_COUNTER_NAME.
     */
    public static final String JAVA_COUNTER_NAME = "java.counter";
    
    /**
     * The constant KOTLIN_COUNTER_NAME.
     */
    public static final String KOTLIN_COUNTER_NAME = "kotlin.counter";
    
    /**
     * The constant SCALA_COUNTER_NAME.
     */
    public static final String SCALA_COUNTER_NAME = "scala.counter";
    
    /**
     * The constant C_COUNTER_NAME.
     */
    public static final String C_COUNTER_NAME = "c.counter";
    
    /**
     * The constant CPP_COUNTER_NAME.
     */
    public static final String CPP_COUNTER_NAME = "cpp.counter";
    
    /**
     * The constant PYTHON_COUNTER_NAME.
     */
    public static final String PYTHON_COUNTER_NAME = "python.counter";
    
    /**
     * The constant GO_COUNTER_NAME.
     */
    public static final String GO_COUNTER_NAME = "go.counter";
    
    /**
     * The constant CS_COUNTER_NAME.
     */
    public static final String CS_COUNTER_NAME = "cs.counter";
    
    /**
     * The constant THROTTLING_COUNTER_NAME.
     */
    public static final String THROTTLING_COUNTER_NAME = "throttling.counter";
    
    /**
     * The constant EXECUTIONS_GAUGE.
     */
    public static final String EXECUTIONS_GAUGE = "executions";
    
    /**
     * The constant SHORT_RUNNING_EXECUTIONS_COUNTER.
     */
    public static final String SHORT_RUNNING_EXECUTIONS_COUNTER = "short-running-executions.counter";
    
    /**
     * The constant LONG_RUNNING_EXECUTIONS_COUNTER.
     */
    public static final String LONG_RUNNING_EXECUTIONS_COUNTER = "long-running-executions.counter";
    
    /**
     * The constant CONTAINER_BUILD_TIMER.
     */
    public static final String CONTAINER_BUILD_TIMER = "container.build";
    
    /**
     * The constant CONTAINER_RUN_TIMER.
     */
    public static final String CONTAINER_RUN_TIMER = "container.build";
    
    /**
     * The constant KAFKA_THROTTLING_RETRIES.
     */
    public static final String KAFKA_THROTTLING_RETRIES = "kafka.throttling.retries";
    
    /**
     * The constant AMQP_THROTTLING_RETRIES.
     */
    public static final String AMQP_THROTTLING_RETRIES = "amqp.throttling.retries";
}
