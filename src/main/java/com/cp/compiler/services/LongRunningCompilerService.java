package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.repositories.HooksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The type Long running compiler service.
 * Used for push notification
 */
@Slf4j
@Service("longRunning")
public class LongRunningCompilerService extends CompilerServiceDecorator {
    
    private final RestTemplate restTemplate;

    private final HooksRepository hooksRepository;
    
    /**
     * Instantiates a new Long running compiler service.
     *
     * @param compilerService the compiler service
     * @param restTemplate    the rest template
     * @param hooksRepository the hooks storage
     */
    public LongRunningCompilerService(@Qualifier("client") CompilerService compilerService,
                                      RestTemplate restTemplate,
                                      HooksRepository hooksRepository) {
        super(compilerService);
        this.restTemplate = restTemplate;
        this.hooksRepository = hooksRepository;
    }
    
    @Override
    public ResponseEntity compile(Execution execution) {
        String url = hooksRepository.get(execution.getId());
        new Thread(() -> {
            try {
                run(execution, url);
            } catch (Exception exception) {
                // Other exception not expected
                // In this case the error will not be returned to the client
                log.error("Error : {}", exception);
            }
        }).start();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Executing the request, you'll get the response in the following url : " + url);
    }
    
    // The check of URI syntax is done before the compilation.
    private void sendResponse(String url, ResponseEntity responseEntity) throws URISyntaxException {
        restTemplate.postForEntity(new URI(url), responseEntity, Object.class);
    }
    
    private void run(Execution execution, String url) throws URISyntaxException {
        ResponseEntity response = getCompilerService().compile(execution);
        log.info("Sending response to {}", url);
        sendResponse(url, response);
    }
}
