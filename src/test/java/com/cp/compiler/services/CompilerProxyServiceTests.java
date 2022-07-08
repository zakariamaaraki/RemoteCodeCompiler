package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import com.cp.compiler.repositories.HooksRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("longRunning")
@DirtiesContext
@SpringBootTest
public class CompilerProxyServiceTests {
    
    private static final int BAD_REQUEST = 400;
    
    @Autowired
    private CompilerProxy compilerProxy;
    
    @MockBean
    private CompilerServiceDefault compilerServiceDefault;
    
    @MockBean
    private LongRunningCompilerService longRunningCompilerService;
    
    @Autowired
    private HooksRepository hooksRepository;
    
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
    void WhenInputFileNameIsInvalidShouldReturnBadRequest() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                validFileName, invalidFileName, validFileName, 10, 500, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerProxy.compile(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    @Test
    void WhenSourceCodeFileNameIsInvalidShouldReturnBadRequest() throws Exception {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                invalidFileName, validFileName, validFileName, 10, 500, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerProxy.compile(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    @Test
    void WhenExpectedOutputFileNameIsInvalidShouldReturnBadRequest() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                validFileName, validFileName, invalidFileName, 10, 500, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerProxy.compile(execution);
        
        // Then
        Assertions.assertEquals(BAD_REQUEST, responseEntity.getStatusCodeValue());
    }
    
    @Test
    void shouldCallLongRunningOperation() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                validFileName, null, validFileName, 10, 500, Language.JAVA);
    
        hooksRepository.addUrl(execution.getId(), "http://localhost");
        
        // When
        ResponseEntity responseEntity = compilerProxy.compile(execution);
    
        // Then
        Mockito.verify(longRunningCompilerService, Mockito.times(1)).compile(execution);
    }
    
    @Test
    void shouldCallDefaultCompiler() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                validFileName, null, validFileName, 10, 500, Language.JAVA);
        
        // When
        ResponseEntity responseEntity = compilerProxy.compile(execution);
        
        // Then
        Mockito.verify(compilerServiceDefault, Mockito.times(1)).compile(execution);
    }
}
