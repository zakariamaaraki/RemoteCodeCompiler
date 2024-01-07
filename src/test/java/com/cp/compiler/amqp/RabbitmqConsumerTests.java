package com.cp.compiler.amqp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.exceptions.CompilerThrottlingException;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.executions.languages.JavaExecution;
import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.testcases.TestCaseResult;
import com.cp.compiler.services.businesslogic.CompilerService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RabbitmqConsumerTests {
    
    @Mock
    private CompilerService compilerService;
    
    @Mock
    private RabbitTemplate rabbitTemplate;
    
    @Mock
    private MeterRegistry meterRegistry;
    
    @Mock
    private Counter counter;
    
    @InjectMocks
    private RabbitConsumer rabbitConsumer;
    
    @TestConfiguration
    static class RabbitConsumerTestContextConfiguration {
        
        @Bean
        public RabbitConsumer rabbitConsumer() {
            return new RabbitConsumer();
        }
    }
    
    @BeforeEach
    public void setUp() {
        rabbitConsumer.init();
    }
    
    @Test
    public void listen_validJsonRequest_transformAndSendCalled() {
        // Arrange
        String jsonRequest = "{\n\"testCases\":{\"test1\":{\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\"}}," +
                "\n\"sourcecode\": \"public class Test1 {\\npublic static void main(String[] args) {\\nint i = 0;\\nwhile (i < 10) " +
                "{\\nSystem.out.println(i++);\\n}}}\",\n\"language\": \"JAVA\",\"timeLimit\": 15,\"memoryLimit\": 500\n}";
    
        ExecutionFactory.registerExecution(
                Language.JAVA,
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        var result =
                new TestCaseResult(Verdict.ACCEPTED, "aaa", "", "aaa", 100);
        LinkedHashMap<String, TestCaseResult> testCasesResult = new LinkedHashMap<>();
        testCasesResult.put("id", result);
    
        var response = new RemoteCodeCompilerExecutionResponse(
                result.getStatusResponse(),
                result.getVerdict().getStatusCode(),
                "",
                testCasesResult,
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        when(compilerService.execute(any())).thenReturn(ResponseEntity.ok(new RemoteCodeCompilerResponse(response)));
        
        // Act
        rabbitConsumer.listen(jsonRequest);
        
        // Assert
        verify(compilerService, times(1)).execute(any());
        verify(rabbitTemplate, times(1)).convertAndSend(any(), anyString());
    }
    
    @Test
    public void listen_throttlingException_retryAfterCalled() {
        // Arrange
        String jsonRequest = "{\n\"testCases\":{\"test1\":{\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\"}}," +
                "\n\"sourcecode\": \"public class Test1 {\\npublic static void main(String[] args) {\\nint i = 0;\\nwhile (i < 10) " +
                "{\\nSystem.out.println(i++);\\n}}}\",\n\"language\": \"JAVA\",\"timeLimit\": 15,\"memoryLimit\": 500\n}";
    
        ExecutionFactory.registerExecution(
                Language.JAVA,
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));
    
        var result =
                new TestCaseResult(Verdict.ACCEPTED, "aaa", "", "aaa", 100);
        LinkedHashMap<String, TestCaseResult> testCasesResult = new LinkedHashMap<>();
        testCasesResult.put("id", result);
    
        var response = new RemoteCodeCompilerExecutionResponse(
                result.getStatusResponse(),
                result.getVerdict().getStatusCode(),
                "",
                testCasesResult,
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        when(compilerService.execute(any()))
                .thenThrow(new CompilerThrottlingException("throttling"))
                .thenReturn(ResponseEntity.ok(new RemoteCodeCompilerResponse(response)));
        
        // Act
        rabbitConsumer.listen(jsonRequest);
        
        // Assert
        verify(compilerService, times(2)).execute(any());
        verify(rabbitTemplate, times(1)).convertAndSend(any(), anyString());
    }
}