package com.cp.compiler.mappers;

import com.cp.compiler.exceptions.ThrottlingException;
import com.cp.compiler.models.*;
import com.cp.compiler.services.CompilerService;
import com.cp.compiler.services.ContainerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.time.LocalDateTime;

@DirtiesContext
@SpringBootTest
public class JsonMapperTests {
    
    @Mock
    private CompilerService compilerService;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        objectMapper.findAndRegisterModules();
    }
    
    private final static Request request = new Request(null, "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n",
            "public class Test1 {\npublic static void main(String[] args) {\nint i = 0;\nwhile (i < 10) " +
                    "{\nSystem.out.println(i++);\n}}}",
            Language.JAVA,
            15,
            500
    );
    
    private final static String jsonRequest = "{\n\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\",\n\"sourceCode\": " +
            "\"public class Test1 {\\npublic static void main(String[] args) {\\nint i = 0;\\nwhile (i < 10) " +
            "{\\nSystem.out.println(i++);\\n}}}\",\n\"language\": \"JAVA\",\"timeLimit\": 15,\"memoryLimit\": 500\n}";
    
    @Test
    void shouldTransformJsonRequestJsonToRequestObject() throws IOException {
        // When
        Request requestInput = JsonMapper.toRequest(jsonRequest);
        
        // Then
        Assertions.assertEquals(request, requestInput);
    }
    
    @Test
    void shouldTransformResponseObjectToJsonResponse() throws JsonProcessingException {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now();
        Result result = new Result(
                Verdict.ACCEPTED,
                "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n",
                "",
                "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n",
                0);
        Response response = new Response(result, localDateTime);
        
        // When
        String responseOutput = JsonMapper.toJson(response);
        
        // Then
        Assertions.assertNotNull(responseOutput);
    }
    
    @Test
    void givenJsonRequestShouldCompileTheRequestAndReturnJsonResponse() throws Exception {
        // Given
        Mockito.when(compilerService.compile(ArgumentMatchers.any()))
                .thenReturn(ResponseEntity.ok(
                        new Response(
                            new Result(Verdict.ACCEPTED, "aaa", "", "aaa", 100),
                            null)));
        
        // When
        String jsonResponse = JsonMapper.transform(jsonRequest, compilerService);
        
        // Then
        Assertions.assertNotNull(jsonResponse);
    }
    
    @Test
    void givenJsonRequestShouldCompileTheRequestAndReturnACorrectJsonResponse() throws Exception {
        // Given
        final var result = new Result(Verdict.ACCEPTED, "aaa", "", "aaa", 100);
    
        Mockito.when(compilerService.compile(ArgumentMatchers.any()))
                .thenReturn(ResponseEntity.ok(new Response(result, LocalDateTime.now())));
        
        // When
        String jsonResponse = JsonMapper.transform(jsonRequest, compilerService);
        
        // Then
        Assertions.assertEquals(result, toResponse(jsonResponse).getResult());
    }
    
    @Test
    void ifTheRequestIsThrottledShouldThrowAThrottlingException() throws Exception {
        // Given
        final var result = new Result(Verdict.ACCEPTED, "aaa", "", "aaa", 100);
    
        Mockito.when(compilerService.compile(ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS));
        
        // Then
        Assertions.assertThrows(ThrottlingException.class, () -> JsonMapper.transform(jsonRequest, compilerService));
    }
    
    @Test
    void shouldReturnNullValueIfTheReturnedObjectIsNotAnInstanceOfResponseClass() throws Exception {

        // Given
        Mockito.when(compilerService.compile(ArgumentMatchers.any()))
                .thenReturn(ResponseEntity.ok("test"));
    
        // When
        var jsonResponse = JsonMapper.transform(jsonRequest, compilerService);
        
        // Then
        Assertions.assertEquals(null, jsonResponse);
    }
    
    private Response toResponse(String jsonResponse) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, Response.class);
    }

}
