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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("longRunning")
@DirtiesContext
@SpringBootTest
public class CompilerFacadeTests {
    
    @MockBean(name = "proxy")
    private CompilerService compilerService;
    
    @Autowired
    private HooksRepository hooksRepository;
    
    @Autowired
    private CompilerFacadeDefault compilerFacade;
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void whenItsShortRunningOperationShouldReturnOKStatusCode() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 500, Language.JAVA);
    
        Mockito.when(compilerService.compile(execution)).thenReturn(ResponseEntity.ok("ok test"));
    
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, false, null);
        
        // Then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    void shouldAddUrlToTheRepository() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 500, Language.JAVA);
        
        String url = "http://localhost";
    
        Mockito.when(compilerService.compile(execution)).thenReturn(ResponseEntity.ok("ok test"));
    
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, true, url);
    
        // Then
        Assertions.assertEquals(url, hooksRepository.get(execution.getId()));
    }
    
    @Test
    void shouldNotAddUrlToTheRepository() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 500, Language.JAVA);
        
        String url = "http://localhost";
        
        Mockito.when(compilerService.compile(execution)).thenReturn(ResponseEntity.ok("ok test"));
        
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, false, url);
        
        // Then
        Assertions.assertNull(hooksRepository.get(execution.getId()));
    }
    
    @Test
    void shouldReturnBadRequest() {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 500, Language.JAVA);
    
        String url = "bad-url";
    
        Mockito.when(compilerService.compile(execution)).thenReturn(ResponseEntity.ok("ok test"));
    
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, true, url);
    
        // Then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
