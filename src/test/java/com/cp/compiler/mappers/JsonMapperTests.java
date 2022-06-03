package com.cp.compiler.mappers;

import com.cp.compiler.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.time.LocalDateTime;

public class JsonMapperTests {
    
    private final static Request request = new Request(null, "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n",
            "public class Test1 {\npublic static void main(String[] args) {\nint i = 0;\nwhile (i < 10) " +
                    "{\nSystem.out.println(i++);\n}}}",
            Language.JAVA,
            15,
            500
    );
    
    @Test
    public void shouldTransformJsonRequestToRequestObject() throws IOException {
        // Given
        String requestJson = "{\n\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\",\n\"sourceCode\": " +
                "\"public class Test1 {\\npublic static void main(String[] args) {\\nint i = 0;\\nwhile (i < 10) " +
                "{\\nSystem.out.println(i++);\\n}}}\",\n\"language\": \"JAVA\",\"timeLimit\": 15,\"memoryLimit\": 500\n}";

        // When
        Request requestInput = JsonMapper.toRequest(requestJson);
        
        // Then
        Assertions.assertEquals(request, requestInput);
    }
    
    @Test
    public void shouldTransformResponseObjectToJsonResponse() throws JsonProcessingException {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now();
        Result result = new Result(
                Verdict.ACCEPTED, "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", 0);
        Response response = new Response(result, localDateTime);
        
        // When
        String responseOutput = JsonMapper.toJson(response);
        
        // Then
        Assertions.assertNotNull(responseOutput);
    }

}
