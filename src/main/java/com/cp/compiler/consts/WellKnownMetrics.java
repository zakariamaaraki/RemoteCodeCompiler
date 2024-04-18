package com.cp.compiler.consts;

/**
 * This class contains all constants related to metrics
 *
 * @author Zakaria Maaraki
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
     * The constant RUST_COUNTER_NAME.
     */
    public static final String RUST_COUNTER_NAME = "rust.counter";
    
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
    public static final String CS_COUNTER_NAME = "csharp.counter";
    
    /**
     * The constant RUBY_COUNTER_NAME.
     */
    public static final String RUBY_COUNTER_NAME = "ruby.counter";
    
    /**
     * The constant HASKELL_COUNTER_NAME.
     */
    public static final String HASKELL_COUNTER_NAME = "haskell.counter";
    
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
    public static final String CONTAINER_RUN_TIMER = "container.run";
    
    /**
     * The constant COMPILATION_TIMER.
     */
    public static final String COMPILATION_TIMER = "compilation.duration";
    
    /**
     * The constant EXECUTION_TIMER.
     */
    public static final String EXECUTION_TIMER = "execution.run";
    
    /**
     * The constant KAFKA_THROTTLING_RETRIES.
     */
    public static final String KAFKA_THROTTLING_RETRIES = "kafka.throttling.retries";
    
    /**
     * The constant AMQP_THROTTLING_RETRIES.
     */
    public static final String AMQP_THROTTLING_RETRIES = "amqp.throttling.retries";
    
    /**
     * The constant ACCEPTED_VERDICT_COUNTER.
     */
    public static final String ACCEPTED_VERDICT_COUNTER = "statusResponse.accepted.counter";
    
    /**
     * The constant WRONG_ANSWER_VERDICT_COUNTER.
     */
    public static final String WRONG_ANSWER_VERDICT_COUNTER = "statusResponse.wrong-answer.counter";
    
    /**
     * The constant COMPILATION_ERROR_VERDICT_COUNTER.
     */
    public static final String COMPILATION_ERROR_VERDICT_COUNTER = "statusResponse.compilation-error.counter";
    
    /**
     * The constant RUNTIME_ERROR_VERDICT_COUNTER.
     */
    public static final String RUNTIME_ERROR_VERDICT_COUNTER = "statusResponse.runtime-error.counter";
    
    /**
     * The constant TIME_LIMIT_EXCEEDED_VERDICT_COUNTER.
     */
    public static final String TIME_LIMIT_EXCEEDED_VERDICT_COUNTER = "statusResponse.time-limit-exceeded.counter";
    
    /**
     * The constant OUT_OF_MEMORY_VERDICT_COUNTER.
     */
    public static final String OUT_OF_MEMORY_VERDICT_COUNTER = "statusResponse.out-of-memory.counter";
}
