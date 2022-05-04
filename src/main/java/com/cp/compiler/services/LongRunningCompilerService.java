package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service("longRunning")
public class LongRunningCompilerService extends CompilerServiceDecorator {
    
    private final RestTemplate restTemplate;

    private final HooksStorage hooksStorage;
    
    public LongRunningCompilerService(@Qualifier("client") CompilerService compilerService,
                                      RestTemplate restTemplate,
                                      HooksStorage hooksStorage) {
        super(compilerService);
        this.restTemplate = restTemplate;
        this.hooksStorage = hooksStorage;
    }
    
    @Override
    public ResponseEntity compile(Execution execution) throws Exception {
        new Thread(() -> {
            try {
                run(execution);
            } catch (Exception exception) {
                // Other exception not expected
                // In this case the error will not be returned to the client
                log.error("Error : {}", exception);
            }
        }).start();
        String url = hooksStorage.get(execution.getImageName());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Executing the request, you'll get the response in the following url : " + url);
    }
    
    // The check of URI syntax is done before the compilation.
    private void sendResponse(String url, ResponseEntity responseEntity) throws URISyntaxException {
        restTemplate.postForEntity(new URI(url), responseEntity, Object.class);
    }
    
    private void run(Execution execution) throws URISyntaxException {
        ResponseEntity response;
        try {
            response = getCompilerService().compile(execution);
        } catch (Exception exception) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
        }
        String imageName = execution.getImageName();
        String url = hooksStorage.get(imageName);
        log.info("Sending response to {}", url);
        sendResponse(url, response);
    }
}
