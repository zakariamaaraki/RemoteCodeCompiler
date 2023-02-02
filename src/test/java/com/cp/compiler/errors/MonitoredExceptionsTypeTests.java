package com.cp.compiler.errors;

import com.cp.compiler.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonitoredExceptionsTypeTests {
    
    @Test
    void whenAMonitoredExceptionIsThrownTheErrorCounterShouldIncrement() {
        // Given
        MonitoredException monitoredException = new CompilationTimeoutException("message");
    
        // When / Then
        Assertions.assertEquals(1, monitoredException.getCounter().count());
        new CompilationTimeoutException("message");
        Assertions.assertEquals(2, monitoredException.getCounter().count());
    }
    
    @Test
    void compilationTimeoutExceptionTypeShouldBeWarning() {
        // Given
        MonitoredException monitoredException = new CompilationTimeoutException("message");
        
        // When / Then
        Assertions.assertEquals(ErrorType.WARNING, monitoredException.getErrorType());
    }
    
    @Test
    void compilerServerInternalExceptionTypeShouldBeError() {
        // Given
        MonitoredException monitoredException = new CompilerServerInternalException("message");
        
        // When / Then
        Assertions.assertEquals(ErrorType.ERROR, monitoredException.getErrorType());
    }
    
    @Test
    void containerBuildExceptionTypeShouldBeError() {
        // Given
        MonitoredException monitoredException = new ContainerBuildException("message");
        
        // When / Then
        Assertions.assertEquals(ErrorType.ERROR, monitoredException.getErrorType());
    }
    
    @Test
    void resourceLimitReachedExceptionTypeShouldBeWarning() {
        // Given
        MonitoredException monitoredException = new ResourceLimitReachedException("message");
        
        // When / Then
        Assertions.assertEquals(ErrorType.WARNING, monitoredException.getErrorType());
    }
    
    @Test
    void throttlingExceptionTypeShouldBeWarning() {
        // Given
        MonitoredException monitoredException = new CompilerThrottlingException("message");
        
        // When / Then
        Assertions.assertEquals(ErrorType.WARNING, monitoredException.getErrorType());
    }
}
