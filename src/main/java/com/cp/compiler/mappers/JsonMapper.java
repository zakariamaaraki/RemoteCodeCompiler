package com.cp.compiler.mappers;

import com.cp.compiler.models.Request;
import com.cp.compiler.models.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}
