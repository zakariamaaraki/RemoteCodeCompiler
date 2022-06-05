package com.cp.compiler.services;

import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.Result;
import com.cp.compiler.models.Verdict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

@DirtiesContext
@SpringBootTest
public class LongRunningCompilerServiceTests {

    @Autowired
    private LongRunningCompilerService compilerService;
    
    @MockBean
    private ContainerService containerService;
    
    @Test
    void registrationTest() {
        Assertions.assertNotNull(compilerService.getCompilerService());
    }
    
    @Test
    void shouldReturnedAcceptedStatusCode() throws Exception {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.txt.c",
                "test.txt",
                null,
                (byte[]) null);
        
        var execution = ExecutionFactory.createExecution(
                file, file, file, 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(0);
    
        Result result = new Result(Verdict.ACCEPTED, "file.txt", "file.txt", 0);
    
        Mockito.when(containerService.runCode(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
                .thenReturn(result);
        
        // When
        var compilationResult = compilerService.compile(execution);
        
        // Then
        Assertions.assertEquals(HttpStatus.ACCEPTED, compilationResult.getStatusCode());
    }
}
