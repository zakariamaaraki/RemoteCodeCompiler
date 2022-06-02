package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class CompilerFacadeTests {
    
    @MockBean(name = "proxy")
    private CompilerService compilerService;
    
    @Autowired
    private CompilerFacade compilerFacade;
    
    private MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            null,
            (byte[]) null);
    
    @Test
    void whenItsShortRunningOperationShouldReturnOKStatusCode() throws Exception {
        // Given
        Execution execution = ExecutionFactory.createExecution(
                file, file, file, 10, 500, Language.JAVA);
    
        Mockito.when(compilerService.compile(execution)).thenReturn(ResponseEntity.ok("ok test"));
    
        // When
        ResponseEntity responseEntity = compilerFacade.compile(execution, true, "testUrl");
        
        // Then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
