package com.cp.compiler.config;

import com.cp.compiler.exceptions.ErrorCode;
import com.cp.compiler.exceptions.ErrorCounterFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonitoredErrorsConfigTests {
    
    @Test
    void allErrorsCounterShouldBeCreatedAtStartUp() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            Assertions.assertNotNull(ErrorCounterFactory.getCounter(errorCode));
        }
    }
    
    @Test
    void checkErrorCounterName() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            Assertions.assertEquals(errorCode.name().toLowerCase(),
                                    ErrorCounterFactory.getCounter(errorCode).getId().getName());
        }
    }
}
