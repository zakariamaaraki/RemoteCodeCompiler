package com.cp.compiler.utilities;

import com.cp.compiler.models.Verdict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The type Status util tests.
 */
class StatusUtilTests {
    /**
     * Should return accepted.
     */
    @Test
    void shouldReturnAccepted() {
        // When
        Verdict status = StatusUtil.statusResponse(StatusUtil.ACCEPTED_OR_WRONG_ANSWER_STATUS, true);
        
        // Then
        Assertions.assertEquals(Verdict.ACCEPTED, status);
    }
    
    /**
     * Should return wrong answer.
     */
    @Test
    void shouldReturnWrongAnswer() {
        // When
        Verdict status = StatusUtil.statusResponse(StatusUtil.ACCEPTED_OR_WRONG_ANSWER_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.WRONG_ANSWER, status);
    }
    
    /**
     * Should return run time error.
     */
    @Test
    void shouldReturnRunTimeError() {
        // When
        Verdict status = StatusUtil.statusResponse(1, false);
        
        // Then
        Assertions.assertEquals(Verdict.RUNTIME_ERROR, status);
    }
    
    /**
     * Should return compilation error.
     */
    @Test
    void shouldReturnCompilationError() {
        // When
        Verdict status = StatusUtil.statusResponse(StatusUtil.COMPILATION_ERROR_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.COMPILATION_ERROR, status);
    }
    
    /**
     * Should return out of memory.
     */
    @Test
    void shouldReturnOutOfMemory() {
        // When
        Verdict status = StatusUtil.statusResponse(StatusUtil.OUT_OF_MEMORY_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.OUT_OF_MEMORY, status);
    }
    
    /**
     * Should return time limit exceeded.
     */
    @Test
    void shouldReturnTimeLimitExceeded() {
        // When
        Verdict status = StatusUtil.statusResponse(StatusUtil.TIME_LIMIT_EXCEEDED_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.TIME_LIMIT_EXCEEDED, status);
    }
}
