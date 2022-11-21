package com.cp.compiler.services;

import com.cp.compiler.exceptions.ContainerBuildException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.*;
import com.cp.compiler.services.containers.ContainerService;
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
class CompilerServiceDecoratorTests {
    
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
            public ResponseEntity execute(Execution execution) {
                return compilerService.execute(execution);
            }
        };
        
        // Then
        Assertions.assertNotNull(compilerServiceDecorator.getCompilerService());
    }
    
    @Test
    void shouldHaveTheSameBehaviorAsTheCompilerClient() throws Exception {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.java",
                "test.java",
                null,
                (byte[]) null);
        
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity execute(Execution execution) {
                return compilerService.execute(execution);
            }
        };
    
        var execution1 = ExecutionFactory.createExecution(file, file, file, 10, 100, Language.JAVA);
        var execution2 = ExecutionFactory.createExecution(file, file, file, 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdErr("")
                .stdOut("test")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();

        Mockito.when(containerService.runContainer(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat()))
                .thenReturn(containerOutput);
        
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
        
        // When
        var compilationResult = compilerServiceDecorator.execute(execution1);
        
        // Then
        Assertions.assertNotNull(compilationResult);
        Assertions.assertEquals(
                ((Response)compilerService.execute(execution2).getBody()).getResult(),
                ((Response)compilationResult.getBody()).getResult()
        );
    }
    
    @Test
    void compilerDecoratorShouldThrowContainerOperationTimeoutException() throws Exception {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.java",
                "test.java",
                null,
                (byte[]) null);
    
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity execute(Execution execution) {
                return compilerService.execute(execution);
            }
        };
    
        var execution = ExecutionFactory.createExecution(
                file, file, file, 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerOperationTimeoutException("exception"));
    
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS).build());
        
        // Then
        Assertions.assertThrows(ContainerBuildException.class, () -> {
            compilerServiceDecorator.execute(execution);
        });
    }
}
