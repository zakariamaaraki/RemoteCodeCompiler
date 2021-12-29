package com.cp.compiler.mappers;

import com.cp.compiler.models.Language;
import com.cp.compiler.models.Request;
import com.cp.compiler.models.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class JsonMapperTests {
	
	private final static Request request = new Request(null, "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n",
			"public class Test1 {\npublic static void main(String[] args) {\nint i = 0;\nwhile (i < 10) {\nSystem.out.println(i++);\n}}}",
			Language.JAVA,
			15,
			500
	);
	
	@Test
	public void shouldTransformJsonRequestToRequestObject() throws IOException {
		// Given
		String requestJson = "{\n\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\",\n\"sourceCode\": \"public class Test1 {\\npublic static void main(String[] args) {\\nint i = 0;\\nwhile (i < 10) {\\nSystem.out.println(i++);\\n}}}\",\n\"language\": \"JAVA\",\"timeLimit\": 15,\"memoryLimit\": 500\n}";
		String responseJson = "{\"output\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\",\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7^\n8^\n9^\n\",\"status\": \"Accepted\",\"date\": [2021,12,1,22,10,24,311828000]}";
		
		// When
		Request requestInput = JsonMapper.toRequest(requestJson);
		
		// Then
		Assertions.assertEquals(requestInput, request);
	}
	
	@Test
	public void shouldTransformResponseObjectToJsonResponse() throws JsonProcessingException {
		// Given
		LocalDateTime localDateTime = LocalDateTime.now();
		Response response = new Response("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", "0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", "Accepted", localDateTime);
		
		// When
		String responseOutput = JsonMapper.toJson(response);
		
		// Then
		Assertions.assertNotNull(responseOutput);
	}

}
