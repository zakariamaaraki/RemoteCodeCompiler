package com.cp.compiler.mappers;

import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.exceptions.CompilerThrottlingException;
import com.cp.compiler.models.*;
import com.cp.compiler.contract.testcases.TestCase;
import com.cp.compiler.contract.testcases.TestCaseResult;
import com.cp.compiler.services.businesslogic.CompilerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

@DirtiesContext
@SpringBootTest
class JsonMapperTests {
    
    @Mock
    private CompilerService compilerService;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private static LinkedHashMap<String, TestCase> testCases;
    
    static {
        objectMapper.findAndRegisterModules();
        testCases = new LinkedHashMap<>();
        testCases.put("test1", new TestCase(null, "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n"));
    }
    
    private final static RemoteCodeCompilerRequest request = new RemoteCodeCompilerRequest(
            "public class Test1 {\npublic static void main(String[] args) {\nint i = 0;\nwhile (i < 10) " +
                    "{\nSystem.out.println(i++);\n}}}",
            Language.JAVA,
            15,
            500,
            testCases
    );
    
    private final static String jsonRequest = "{\n\"testCases\":{\"test1\":{\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\"}}," +
            "\n\"sourcecode\": \"public class Test1 {\\npublic static void main(String[] args) {\\nint i = 0;\\nwhile (i < 10) " +
            "{\\nSystem.out.println(i++);\\n}}}\",\n\"language\": \"JAVA\",\"timeLimit\": 15,\"memoryLimit\": 500\n}";
    
    @Test
    void shouldTransformJsonRequestJsonToRequestObject() throws IOException {
        // When
        RemoteCodeCompilerRequest requestInput = JsonMapper.toRequest(jsonRequest);
        
        // Then
        Assertions.assertEquals(request, requestInput);
    }
    
    @Test
    void shouldTransformResponseObjectToJsonResponse() throws JsonProcessingException {
        // Given
        TestCaseResult result = new TestCaseResult(
                Verdict.ACCEPTED,
                "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n",
                "",
                "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n",
                0);
        
        LinkedHashMap<String, TestCaseResult> testCasesResult = new LinkedHashMap<>();
        testCasesResult.put("id", result);
    
        var response = new RemoteCodeCompilerExecutionResponse(
                result.getVerdict().getStatusResponse(),
                result.getVerdict().getStatusCode(),
                "",
                testCasesResult,
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        // When
        String responseOutput = JsonMapper.toJson(new RemoteCodeCompilerResponse(response));
        
        // Then
        Assertions.assertNotNull(responseOutput);
    }
    
    @Test
    void givenJsonRequestShouldCompileTheRequestAndReturnJsonResponse() throws Exception {
        // Given
        var result =
                new TestCaseResult(Verdict.ACCEPTED, "aaa", "", "aaa", 100);
        LinkedHashMap<String, TestCaseResult> testCasesResult = new LinkedHashMap<>();
        testCasesResult.put("id", result);
    
        var response = new RemoteCodeCompilerExecutionResponse(
                result.getVerdict().getStatusResponse(),
                result.getVerdict().getStatusCode(),
                "",
                testCasesResult,
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        Mockito.when(compilerService.execute(ArgumentMatchers.any()))
                .thenReturn(ResponseEntity.ok(new RemoteCodeCompilerResponse(response)));
        
        // When
        String jsonResponse = JsonMapper.transform(jsonRequest, compilerService);
        
        // Then
        Assertions.assertNotNull(jsonResponse);
    }
    
    @Test
    void givenJsonRequestShouldCompileTheRequestAndReturnACorrectJsonResponse() throws Exception {
        // Given
        var result =
                new TestCaseResult(Verdict.ACCEPTED, "aaa", "", "aaa", 100);
        LinkedHashMap<String, TestCaseResult> testCasesResult = new LinkedHashMap<>();
        testCasesResult.put("id", result);
    
        var response = new RemoteCodeCompilerExecutionResponse(
                result.getVerdict().getStatusResponse(),
                result.getVerdict().getStatusCode(),
                "",
                testCasesResult,
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
    
        Mockito.when(compilerService.execute(ArgumentMatchers.any()))
                .thenReturn(ResponseEntity.ok(new RemoteCodeCompilerResponse(response)));
        
        // When
        String jsonResponse = JsonMapper.transform(jsonRequest, compilerService);
        
        // Then
        Assertions.assertEquals(response, toResponse(jsonResponse).getExecution());
    }
    
    @Test
    void ifTheRequestIsThrottledShouldThrowAThrottlingException() {
        // Given
        Mockito.when(compilerService.execute(ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS));
        
        // When / Then
        Assertions.assertThrows(CompilerThrottlingException.class, () -> JsonMapper.transform(jsonRequest, compilerService));
    }
    
    private RemoteCodeCompilerResponse toResponse(String jsonResponse) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, RemoteCodeCompilerResponse.class);
    }
}
