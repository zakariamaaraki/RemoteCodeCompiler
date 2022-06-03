package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class RegisteredFactoriesTests {
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void javaExecutionFactoryShouldBeRegistered() {
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.JAVA));
    }
    
    @Test
    void cExecutionFactoryShouldBeRegistered() {
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.C));
    }
    
    @Test
    void cppExecutionFactoryShouldBeRegistered() {
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.CPP));
    }
    
    @Test
    void pythonExecutionFactoryShouldBeRegistered() {
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.PYTHON));
    }
}
