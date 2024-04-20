package com.cp.compiler.services;

import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.services.businesslogic.CompilerService;
import com.cp.compiler.services.businesslogic.CompilerServiceDecorator;
import com.cp.compiler.services.platform.containers.ContainerService;
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

import java.util.List;

@DirtiesContext
@SpringBootTest
class CompilerServiceDecoratorTests {
    
    @Autowired
    @Qualifier("client")
    private CompilerService compilerService;
    
    @MockBean
    private ContainerService containerService;

    @DirtiesContext
    @Test
    void registrationTest() {
        // When
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity<RemoteCodeCompilerResponse> execute(Execution execution) {
                return compilerService.execute(execution);
            }
        };
        
        // Then
        Assertions.assertNotNull(compilerServiceDecorator.getCompilerService());
    }

    @DirtiesContext
    @Test
    void shouldHaveTheSameBehaviorAsTheCompilerClient() {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.java",
                "test.java",
                null,
                (byte[]) null);
        
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity<RemoteCodeCompilerResponse> execute(Execution execution) {
                return compilerService.execute(execution);
            }
        };
    
        var testCase1 = new TransformedTestCase("id", file, "test");
        var testCase2 = new TransformedTestCase("id", file, "test");
    
        var execution1 =
                ExecutionFactory.createExecution(file, List.of(testCase1), 10, 100, Language.JAVA);
        var execution2 =
                ExecutionFactory.createExecution(file, List.of(testCase2), 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdErr("")
                .stdOut("test")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();

        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
        
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
        
        // When
        var compilationResult = compilerServiceDecorator.execute(execution1);
        
        // Then
        Assertions.assertNotNull(compilationResult);
        Assertions.assertEquals(
                compilerService
                        .execute(execution2)
                        .getBody()
                        .getExecution()
                        .getTestCasesResult(),
                compilationResult
                        .getBody()
                        .getExecution()
                        .getTestCasesResult()
        );
    }

    @DirtiesContext
    @Test
    void compilerDecoratorShouldThrowContainerOperationTimeoutException() {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.java",
                "test.java",
                null,
                (byte[]) null);
    
        var compilerServiceDecorator = new CompilerServiceDecorator(compilerService) {
            @Override
            public ResponseEntity<RemoteCodeCompilerResponse> execute(Execution execution) {
                return compilerService.execute(execution);
            }
        };
    
        var testCase = new TransformedTestCase("id", file, "test");
    
        var execution =
                ExecutionFactory.createExecution(file, List.of(testCase), 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenThrow(new ContainerOperationTimeoutException("exception"));
    
        // Should compile
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()))
                .thenReturn(ProcessOutput.builder().status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS).build());
        
        // Then
        Assertions.assertThrows(ContainerOperationTimeoutException.class, () -> {
            compilerServiceDecorator.execute(execution);
        });
    }
}
