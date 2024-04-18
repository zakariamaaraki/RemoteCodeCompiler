package com.cp.compiler.models;

import com.cp.compiler.consts.WellKnownMetrics;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Verdict.
 * It's represents the verdict response.
 *
 * @author Zakaria Maaraki
 */
@AllArgsConstructor
public enum Verdict {
    
    /**
     * Accepted statusResponse.
     */
    ACCEPTED("Accepted", 100, WellKnownMetrics.ACCEPTED_VERDICT_COUNTER),
    /**
     * Wrong answer statusResponse.
     */
    WRONG_ANSWER("Wrong Answer", 200, WellKnownMetrics.WRONG_ANSWER_VERDICT_COUNTER),
    /**
     * Compilation error statusResponse.
     */
    COMPILATION_ERROR("Compilation Error", 300, WellKnownMetrics.COMPILATION_ERROR_VERDICT_COUNTER),
    /**
     * Out of memory statusResponse.
     */
    OUT_OF_MEMORY("Out Of Memory", 400, WellKnownMetrics.OUT_OF_MEMORY_VERDICT_COUNTER),
    /**
     * Time limit exceeded statusResponse.
     */
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", 500, WellKnownMetrics.TIME_LIMIT_EXCEEDED_VERDICT_COUNTER),
    /**
     * Runtime error statusResponse.
     */
    RUNTIME_ERROR("Runtime Error", 600, WellKnownMetrics.RUNTIME_ERROR_VERDICT_COUNTER);
    
    @Getter
    private String statusResponse;
    
    @Getter
    private int statusCode;
    
    @Getter
    private String counterMetric;
}
