package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.WellKnownUrls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * The type Compiler facade.
 */
@Slf4j
@Service
public class CompilerFacadeImpl implements CompilerFacade {
    
    private final CompilerService compilerService;
    
    private final HooksStorage hooksStorage;
    
    @Value("${compiler.features.push-notification.enabled}")
    private boolean isPushNotificationEnabled;
    
    /**
     * Instantiates a new Compiler facade.
     *
     * @param compilerService the compiler service
     * @param hooksStorage    the hooks storage
     */
    public CompilerFacadeImpl(@Qualifier("proxy") CompilerService compilerService, HooksStorage hooksStorage) {
        this.compilerService = compilerService;
        this.hooksStorage = hooksStorage;
    }
    
    @Override
    public ResponseEntity compile(Execution execution, boolean isLongRunning, String url) throws Exception {
        if (isPushNotificationEnabled && isLongRunning) {
            String imageName = execution.getImageName();
            // Check if the url is valid
            if (!isUrlValid(url)) {
                return ResponseEntity
                        .badRequest()
                        .body("url " + url  + " not valid");
            }
            log.info(imageName + " The execution is long running and the url is valid");
            hooksStorage.addUrl(execution.getImageName(), url);
        }
        return compilerService.compile(execution);
    }
    
    private boolean isUrlValid(String url) {
        return url.matches(WellKnownUrls.URL_REGEX);
    }
}
