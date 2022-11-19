package com.cp.compiler.utils;

import com.cp.compiler.utils.retries.RetryHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

public class RetryHelperTests {
    
    @Test
    void shouldThrowAnException() {
        Assertions.assertThrows(Exception.class, () -> {
            RetryHelper.executeWithRetries(() -> {
                throw new Exception("exception message");
            }, null, 1, 1000);
        });
    }
    
    @Test
    void shouldRetry3TimesBeforeThrowingAnException() throws Exception {
        retryNTimeBeforeThrowingAnException(3);
    }
    
    @Test
    void shouldNotRetry() throws Exception {
        retryNTimeBeforeThrowingAnException(0);
    }
    
    @Test
    void givenAnExceptionInTheAllowListShouldThrowAnExceptionWithoutRetry() throws Exception {
        // Given
        var dummyMock = Mockito.mock(DummyClass.class);
        Mockito.when(dummyMock.dummyMethod()).thenThrow(new Exception("Exception from dummy class"));
    
        // When / Then
        Assertions.assertThrows(Exception.class, () -> {
            RetryHelper.executeWithRetries(
                    () -> dummyMock.dummyMethod(),
                    Set.of(Exception.class.getName()),
                    4,
                    1000);
        });
    
        Mockito.verify(dummyMock, Mockito.times(1)).dummyMethod();
    }
    
    @Test
    void givenNegativeMaxRetriesShouldThrowIllegalArgumentException() {
        // Given
        int maxRetries = -1;
        
        // When / Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RetryHelper.executeWithRetries(
                    () -> "dummy method",
                    Set.of(Exception.class.getName()),
                    maxRetries,
                    1000);
        });
    }
    
    @Test
    void givenNegativeDurationBetweenEachRetryShouldThrowIllegalArgumentException() {
        // Given
        int durationBetweenEachRetry = -1;
        
        // When / Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RetryHelper.executeWithRetries(
                    () -> "dummy method",
                    Set.of(Exception.class.getName()),
                    3,
                    durationBetweenEachRetry);
        });
    }
    
    private void retryNTimeBeforeThrowingAnException(int numberOfRetry) throws Exception {
        // Given
        var dummyMock = Mockito.mock(DummyClass.class);
        Mockito.when(dummyMock.dummyMethod()).thenThrow(new Exception("Exception from dummy class"));
    
        // When / Then
        Assertions.assertThrows(Exception.class, () -> {
            RetryHelper.executeWithRetries(
                    () -> dummyMock.dummyMethod(),
                    null,
                    numberOfRetry,
                    1000);
        });
        
        Mockito.verify(dummyMock, Mockito.times(numberOfRetry + 1)).dummyMethod();
    }
    
    /**
     * Dummy class used for tests
     */
     private class DummyClass {
        
        public String dummyMethod() throws Exception{
            throw new Exception("Exception from dummy class");
        }
    }
}
