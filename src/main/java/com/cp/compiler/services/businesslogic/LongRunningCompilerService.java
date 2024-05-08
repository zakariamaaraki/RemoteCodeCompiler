package com.cp.compiler.services.businesslogic;

import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.repositories.hooks.HooksRepository;
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
 *
 * @author Zakaria Maaraki
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
    public ResponseEntity<RemoteCodeCompilerResponse> execute(Execution execution) {
        String url = hooksRepository.get(execution.getId());
        new Thread(() -> {
            try {
                run(execution, url);
            } catch (Exception exception) {
                // Unexpected error
                // In this case the error will not be returned to the client
                log.error("Error : {}", exception);
            }
        }).start();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("url", url)
                .body(new RemoteCodeCompilerResponse());
    }
    
    // The check of URI syntax is done before the compilation.
    private void sendResponse(String url, ResponseEntity<RemoteCodeCompilerResponse> responseEntity) throws URISyntaxException {
        restTemplate.postForEntity(new URI(url), responseEntity, Object.class);
    }
    
    private void run(Execution execution, String url) throws URISyntaxException {
        ResponseEntity<RemoteCodeCompilerResponse> response = getCompilerService().execute(execution);
        log.info("Sending response to {}", url);
        sendResponse(url, response);
    }
}
