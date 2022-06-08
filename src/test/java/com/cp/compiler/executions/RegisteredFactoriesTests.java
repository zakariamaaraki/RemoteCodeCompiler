package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

@DirtiesContext
@SpringBootTest
public class RegisteredFactoriesTests {
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void javaExecutionFactoryShouldBeRegistered() {
        Assertions.assertTrue(ExecutionFactory.getRegisteredFactories().contains(Language.JAVA));
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.JAVA));
    }
    
    @Test
    void kotlinExecutionFactoryShouldBeRegistered() {
        Assertions.assertTrue(ExecutionFactory.getRegisteredFactories().contains(Language.KOTLIN));
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.KOTLIN));
    }
    
    @Test
    void csExecutionFactoryShouldBeRegistered() {
        Assertions.assertTrue(ExecutionFactory.getRegisteredFactories().contains(Language.CS));
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.CS));
    }
    
    @Test
    void goExecutionFactoryShouldBeRegistered() {
        Assertions.assertTrue(ExecutionFactory.getRegisteredFactories().contains(Language.GO));
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.GO));
    }
    
    @Test
    void cExecutionFactoryShouldBeRegistered() {
        Assertions.assertTrue(ExecutionFactory.getRegisteredFactories().contains(Language.C));
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.C));
    }
    
    @Test
    void cppExecutionFactoryShouldBeRegistered() {
        Assertions.assertTrue(ExecutionFactory.getRegisteredFactories().contains(Language.CPP));
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.CPP));
    }
    
    @Test
    void pythonExecutionFactoryShouldBeRegistered() {
        Assertions.assertTrue(ExecutionFactory.getRegisteredFactories().contains(Language.PYTHON));
        Assertions.assertNotNull(
                ExecutionFactory.createExecution(file, file, file, 10, 100, Language.PYTHON));
    }
}
