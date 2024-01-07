package com.cp.compiler.mappers;

import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.exceptions.CompilerThrottlingException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.services.businesslogic.CompilerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * The type Json mapper.
 *
 * @author Zakaria Maaraki
 */
public abstract class JsonMapper {
    
    private JsonMapper() {}
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        objectMapper.findAndRegisterModules();
    }
    
    /**
     * To json string.
     *
     * @param response the response
     * @return the string
     * @throws JsonProcessingException the json processing exception
     */
    public static String toJson(RemoteCodeCompilerResponse response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }
    
    /**
     * To request request.
     *
     * @param jsonValue the json value
     * @return the request
     * @throws IOException the io exception
     */
    public static RemoteCodeCompilerRequest toRequest(String jsonValue) throws IOException {
        return objectMapper.readValue(jsonValue, RemoteCodeCompilerRequest.class);
    }
    
    /**
     * Transform string.
     *
     * @param jsonRequest     the json request
     * @param compilerService the compiler service
     * @return the string
     * @throws Exception the exception
     */
    public static String transform(String jsonRequest, CompilerService compilerService) throws Exception {
        RemoteCodeCompilerRequest request = JsonMapper.toRequest(jsonRequest);
        
        Execution execution = ExecutionFactory.createExecution(request.getSourcecodeFile(),
                                                                request.getConvertedTestCases(),
                                                                request.getTimeLimit(),
                                                                request.getMemoryLimit(),
                                                                request.getLanguage());
    
        try(MDC.MDCCloseable mdc = MDC.putCloseable("compiler.language", execution.getLanguage().toString())) {
            
            ResponseEntity<RemoteCodeCompilerResponse> responseEntity = compilerService.execute(execution);
    
            // Throw an exception if the request has been throttled, to keep the request for retries
            if (responseEntity.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                throw new CompilerThrottlingException("The request has been throttled, maximum number of requests has been reached");
            }
            // TODO: add error handling
            return JsonMapper.toJson(responseEntity.getBody());
        }
    }
}
