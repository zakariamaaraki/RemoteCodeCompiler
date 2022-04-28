package com.cp.compiler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Verdict.
 *
 * @author Zakaria Maaraki
 */
@AllArgsConstructor
public enum Verdict {
    
    /**
     * Accepted verdict.
     */
    ACCEPTED("Accepted", 100),
    /**
     * Wrong answer verdict.
     */
    WRONG_ANSWER("Wrong Answer", 200),
    /**
     * Compilation error verdict.
     */
    COMPILATION_ERROR("Compilation Error", 300),
    /**
     * Out of memory verdict.
     */
    OUT_OF_MEMORY("Out Of Memory", 400),
    /**
     * Time limit exceeded verdict.
     */
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", 500),
    /**
     * Runtime error verdict.
     */
    RUNTIME_ERROR("Runtime Error", 600);
    
    @Getter
    private String statusResponse;
    
    @Getter
    private int statusCode;
}
