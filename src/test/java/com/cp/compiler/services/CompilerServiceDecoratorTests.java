package com.cp.compiler.services;

import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.*;
import com.cp.compiler.utils.StatusUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

@DirtiesContext
@SpringBootTest
public class CompilerServiceDecoratorTests {
    
    @Autowired
    @Qualifier("client")
    private CompilerService compilerService;
    
    @MockBean
    private ContainerService containerService;
    
    @Test
    void registrationTest() {
        // When
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity compile(Execution execution) {
                return compilerService.compile(execution);
            }
        };
        
        // Then
        Assertions.assertNotNull(compilerServiceDecorator.getCompilerService());
    }
    
    @Test
    void shouldHaveTheSameBehaviorAsTheCompilerClient() {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.txt.c",
                "test.txt",
                null,
                (byte[]) null);
        
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity compile(Execution execution) {
                return compilerService.compile(execution);
            }
        };
    
        var execution = ExecutionFactory.createExecution(
                file, file, file, 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdOut("test")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
        
        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong()))
                .thenReturn(containerOutput);
        
        // When
        var compilationResult = compilerServiceDecorator.compile(execution);
        
        // Then
        Assertions.assertNotNull(compilationResult);
        Assertions.assertEquals(
                ((Response)compilerService.compile(execution).getBody()).getResult(),
                ((Response)compilationResult.getBody()).getResult()
        );
    }
    
    @Test
    void compilerDecoratorShouldThrowContainerOperationTimeoutException() {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.txt.c",
                "test.txt",
                null,
                (byte[]) null);
    
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity compile(Execution execution) {
                return compilerService.compile(execution);
            }
        };
    
        var execution = ExecutionFactory.createExecution(
                file, file, file, 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerOperationTimeoutException("exception"));
    
        // Then
        Assertions.assertThrows(ContainerOperationTimeoutException.class, () -> {
            compilerServiceDecorator.compile(execution);
        });
    }
}
