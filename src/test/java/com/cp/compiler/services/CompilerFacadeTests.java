package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import com.cp.compiler.repositories.HooksRepository;
import com.cp.compiler.services.businesslogic.CompilerFacadeDefault;
import com.cp.compiler.services.businesslogic.CompilerService;
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

import java.io.IOException;
import java.util.List;

@ActiveProfiles("longRunning")
@DirtiesContext
@SpringBootTest
class CompilerFacadeTests {
    
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
    void whenItsShortRunningOperationShouldReturnOKStatusCode() throws IOException {
        // Given
        var testCase = new ConvertedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
    
        Mockito.when(compilerService.execute(execution)).thenReturn(ResponseEntity.ok("ok test"));
    
        // When
        ResponseEntity responseEntity =
                compilerFacade.compile(execution, false, null, "");
        
        // Then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    void shouldAddUrlToTheRepository() throws IOException {
        // Given
        var testCase = new ConvertedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
        
        String url = "http://localhost";
    
        Mockito.when(compilerService.execute(execution)).thenReturn(ResponseEntity.ok("ok test"));
    
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, true, url, "");
    
        // Then
        Assertions.assertEquals(url, hooksRepository.get(execution.getId()));
    }
    
    @Test
    void shouldNotAddUrlToTheRepository() throws IOException {
        // Given
        var testCase = new ConvertedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
        
        String url = "http://localhost";
        
        Mockito.when(compilerService.execute(execution)).thenReturn(ResponseEntity.ok("ok test"));
        
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, false, url, "");
        
        // Then
        Assertions.assertNull(hooksRepository.get(execution.getId()));
    }
    
    @Test
    void ifUrlIsNotValidShouldReturnBadRequest() throws IOException {
        // Given
        var testCase = new ConvertedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
    
        String url = "bad-url";
    
        Mockito.when(compilerService.execute(execution)).thenReturn(ResponseEntity.ok("ok test"));
    
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, true, url, "");
    
        // Then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    void ifUrlIsNullShouldReturnBadRequest() throws IOException {
        // Given
        var testCase = new ConvertedTestCase("id", file, "test");
        Execution execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 500, Language.JAVA);
        
        String url = null;
        
        Mockito.when(compilerService.execute(execution)).thenReturn(ResponseEntity.ok("ok test"));
        
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, true, url, "");
        
        // Then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
