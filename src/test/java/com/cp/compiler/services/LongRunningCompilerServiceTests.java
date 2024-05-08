package com.cp.compiler.services;

import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.contract.Language;
import com.cp.compiler.repositories.hooks.HooksRepository;
import com.cp.compiler.services.businesslogic.LongRunningCompilerService;
import com.cp.compiler.services.platform.containers.ContainerService;
import com.cp.compiler.utils.StatusUtils;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@DirtiesContext
@SpringBootTest
class LongRunningCompilerServiceTests {

    @Autowired
    private LongRunningCompilerService compilerService;
    
    @MockBean
    private ContainerService containerService;
    
    @MockBean
    private RestTemplate restTemplate;
    
    @MockBean
    private HooksRepository hooksRepository;
    
    @Test
    void registrationTest() {
        Assertions.assertNotNull(compilerService.getCompilerService());
    }
    
    @Test
    void shouldReturnedAcceptedStatusCode() {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.txt.c",
                "test.txt",
                null,
                (byte[]) null);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        var execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 100, Language.JAVA);
    
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");

        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdOut("test")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
        
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
        
        // When
        var compilationResult = compilerService.execute(execution);
        
        // Then
        Assertions.assertEquals(HttpStatus.ACCEPTED, compilationResult.getStatusCode());
    }
    
    @Test
    void getUrlShouldBeCalled() throws Exception {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.c",
                "test.c",
                null,
                (byte[]) null);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        var execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 100, Language.JAVA);
        
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdOut("test")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
        
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.any(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
    
        Mockito.when(hooksRepository.get(ArgumentMatchers.any())).thenReturn("http://localhost/post");
        
        // When
        var compilationResult = compilerService.execute(execution);
        
        // Wait for the end of execution
        Thread.sleep(2000);
        
        // Then
        Mockito.verify(hooksRepository)
                .get(ArgumentMatchers.any());
    }
    
    @Test
    void shouldSendTheResponseToTheCaller() throws Exception {
        // Given
        MultipartFile file = new MockMultipartFile(
                "test.c",
                "test.c",
                null,
                (byte[]) null);
    
        var testCase = new TransformedTestCase("id", file, "test");
        
        var execution = ExecutionFactory.createExecution(
                file, List.of(testCase), 10, 100, Language.JAVA);
        
        Mockito.when(containerService.buildImage(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn("build log");
        
        ProcessOutput containerOutput = ProcessOutput
                .builder()
                .stdErr("")
                .stdOut("test")
                .status(StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS)
                .build();
        
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyFloat(),
                ArgumentMatchers.anyMap())).thenReturn(containerOutput);
    
        Mockito.when(containerService.runContainer(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(containerOutput);
    
        Mockito.when(hooksRepository.get(ArgumentMatchers.any())).thenReturn("http://localhost/post");
        
        // When
        compilerService.execute(execution);
        
        // Wait for the end of execution
        Thread.sleep(2000);
        
        // Then
        Mockito.verify(restTemplate)
                .postForEntity(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
