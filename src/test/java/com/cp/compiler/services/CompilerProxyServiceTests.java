package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class CompilerProxyServiceTests {
    
    private static final int BAD_REQUEST = 400;
    
    @MockBean
    private ContainerService containerService;
    
    @Qualifier("proxy")
    @Autowired
    private CompilerService compilerService;
    
    private MultipartFile invalidFileName = new MockMultipartFile(
            "test",
            "",
            null,
            (byte[]) null);
    
    private MultipartFile validFileName = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void WhenInputFileNameIsInvalidShouldReturnBadRequest() throws Exception {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                validFileName, invalidFileName, validFileName, 10, 500, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.compile(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    @Test
    void WhenSourceCodeFileNameIsInvalidShouldReturnBadRequest() throws Exception {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                invalidFileName, validFileName, validFileName, 10, 500, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.compile(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    @Test
    void WhenExpectedOutputFileNameIsInvalidShouldReturnBadRequest() throws Exception {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                validFileName, validFileName, invalidFileName, 10, 500, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerService.compile(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
}
