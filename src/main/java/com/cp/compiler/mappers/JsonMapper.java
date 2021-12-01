package com.cp.compiler.mappers;

import com.cp.compiler.exceptions.CompilerServerException;
import com.cp.compiler.models.Request;
import com.cp.compiler.models.Response;
import com.cp.compiler.services.CompilerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class JsonMapper {
	
	private JsonMapper() {}
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.findAndRegisterModules();
	}
	
	public static String toJson(Response response) throws JsonProcessingException {
		return objectMapper.writeValueAsString(response);
	}
	
	public static Request toRequest(String jsonValue) throws IOException {
		return objectMapper.readValue(jsonValue, Request.class);
	}
	
	public static String transform(String jsonRequest, CompilerService compilerService) throws IOException, CompilerServerException {
		Request request = JsonMapper.toRequest(jsonRequest);
		ResponseEntity<Object> responseEntity = compilerService.compile(request.getExpectedOutput(),
				request.getSourceCode(), request.getInput(), request.getTimeLimit(), request.getMemoryLimit(),
				request.getLanguage());
		Object body = responseEntity.getBody();
		return body instanceof Response ? JsonMapper.toJson((Response) body) : null;
	}
}
