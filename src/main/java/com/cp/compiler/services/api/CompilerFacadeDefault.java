package com.cp.compiler.services.api;

import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.exceptions.CompilerBadRequestException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.services.businesslogic.CompilerService;
import com.cp.compiler.consts.WellKnownLoggingKeys;
import com.cp.compiler.consts.WellKnownMetrics;
import com.cp.compiler.consts.WellKnownUrls;
import com.cp.compiler.repositories.hooks.HooksRepository;
import com.google.common.io.Closer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * The type Compiler facade.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Service
public class CompilerFacadeDefault implements CompilerFacade {
    
    private static final int MAX_USER_ID_LENGTH = 50;
    
    private final CompilerService compilerService;
    
    private final HooksRepository hooksRepository;
    
    private final MeterRegistry meterRegistry;
    
    @Value("${compiler.features.push-notification.enabled}")
    private boolean isPushNotificationEnabled;
    
    private Counter shortRunningExecutionCounter;
    
    private Counter longRunningExecutionCounter;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        shortRunningExecutionCounter = meterRegistry.counter(WellKnownMetrics.SHORT_RUNNING_EXECUTIONS_COUNTER);
        longRunningExecutionCounter = meterRegistry.counter(WellKnownMetrics.LONG_RUNNING_EXECUTIONS_COUNTER);
    }
    
    /**
     * Instantiates a new Compiler facade.
     *
     * @param compilerService the compiler service
     * @param meterRegistry   the meter registry
     * @param hooksRepository the hooks storage
     */
    public CompilerFacadeDefault(@Qualifier("proxy") CompilerService compilerService,
                                 MeterRegistry meterRegistry,
                                 HooksRepository hooksRepository) {
        this.compilerService = compilerService;
        this.meterRegistry = meterRegistry;
        this.hooksRepository = hooksRepository;
    }
    
    @Override
    public ResponseEntity<RemoteCodeCompilerResponse> compile(
            Execution execution,
            boolean isLongRunning,
            String url,
            String userId) throws IOException {
        
        if (userId == null) {
            userId = "null";
        }
        
        try(Closer closer = Closer.create()) {
    
            if (userId.length() > MAX_USER_ID_LENGTH) {
                userId = userId.substring(MAX_USER_ID_LENGTH);
            }
    
            closer.register(MDC.putCloseable(WellKnownLoggingKeys.USER_ID, userId));
            closer.register(MDC.putCloseable(WellKnownLoggingKeys.IS_LONG_RUNNING, String.valueOf(isLongRunning)));
            closer.register(MDC.putCloseable(WellKnownLoggingKeys.PROGRAMMING_LANGUAGE, execution.getLanguage().toString()));
            
            if (isPushNotificationEnabled && isLongRunning) {
                // Long running execution (Push notification)
                longRunningExecutionCounter.increment();
                // Check if the url is valid
                if (!isUrlValid(url)) {
                    var errorMessage = "url " + url  + " not valid";
                    log.warn(errorMessage);
                    throw new CompilerBadRequestException(errorMessage);
                }
                log.info("The execution is long running and the url is valid");
                hooksRepository.addUrl(execution.getId(), url);
            } else {
                // Short running execution (Long Polling)
                shortRunningExecutionCounter.increment();
            }
            return compilerService.execute(execution);
        }
    }
    
    private boolean isUrlValid(String url) {
        return url != null && url.matches(WellKnownUrls.URL_REGEX);
    }
}
