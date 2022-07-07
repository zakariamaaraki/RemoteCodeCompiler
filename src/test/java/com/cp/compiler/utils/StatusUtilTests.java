package com.cp.compiler.utils;

import com.cp.compiler.models.Verdict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusUtilTests {
    
    @Test
    void shouldReturnAccepted() {
        // When
        Verdict status = StatusUtils.statusResponse(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS, true);
        
        // Then
        Assertions.assertEquals(Verdict.ACCEPTED, status);
    }
    
    @Test
    void shouldReturnWrongAnswer() {
        // When
        Verdict status = StatusUtils.statusResponse(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.WRONG_ANSWER, status);
    }
    
    @Test
    void shouldReturnRunTimeError() {
        // When
        Verdict status = StatusUtils.statusResponse(1, false);
        
        // Then
        Assertions.assertEquals(Verdict.RUNTIME_ERROR, status);
    }
    
    @Test
    void shouldReturnCompilationError() {
        // When
        Verdict status = StatusUtils.statusResponse(StatusUtils.COMPILATION_ERROR_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.COMPILATION_ERROR, status);
    }
    
    @Test
    void shouldReturnOutOfMemory() {
        // When
        Verdict status = StatusUtils.statusResponse(StatusUtils.OUT_OF_MEMORY_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.OUT_OF_MEMORY, status);
    }
    
    @Test
    void shouldReturnTimeLimitExceeded() {
        // When
        Verdict status = StatusUtils.statusResponse(StatusUtils.TIME_LIMIT_EXCEEDED_STATUS, false);
        
        // Then
        Assertions.assertEquals(Verdict.TIME_LIMIT_EXCEEDED, status);
    }
}
