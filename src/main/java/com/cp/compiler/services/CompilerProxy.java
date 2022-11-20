package com.cp.compiler.services;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Language;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.cp.compiler.wellknownconstants.WellKnownMetrics;
import com.cp.compiler.repositories.HooksRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * The Compiler proxy Service.
 */
@Slf4j
@Service("proxy")
public class CompilerProxy implements CompilerService {
    
    @Getter
    @Value("${compiler.execution-memory.max:10000}")
    private int maxExecutionMemory;
    
    @Getter
    @Value("${compiler.execution-memory.min:0}")
    private int minExecutionMemory;
    
    @Getter
    @Value("${compiler.execution-time.max:15}")
    private int maxExecutionTime;
    
    @Getter
    @Value("${compiler.execution-time.min:0}")
    private int minExecutionTime;
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    // For Long Polling
    @Autowired
    @Qualifier("client")
    private CompilerService compilerService;
    
    // For Push Notifications
    @Autowired
    @Qualifier("longRunning")
    private CompilerService longRunningCompilerService;
    
    @Autowired
    private HooksRepository hooksRepository;
    
    @Autowired
    private Resources resources;
    
    private Counter throttlingCounterMetric;
    
    private static final String EXECUTIONS_GAUGE_DESCRIPTION = "Current number of executions";
    
    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        throttlingCounterMetric = meterRegistry.counter(WellKnownMetrics.THROTTLING_COUNTER_NAME);
        Gauge.builder(WellKnownMetrics.EXECUTIONS_GAUGE, () -> resources.getNumberOfExecutions())
                .description(EXECUTIONS_GAUGE_DESCRIPTION)
                .register(meterRegistry);
    }
    
    @Override
    public ResponseEntity execute(Execution execution) {
        Optional<ResponseEntity<Object>> requestValidationError = validateRequest(execution);
        if (requestValidationError.isPresent()) {
            // The request is not valid
            log.info("Invalid input data: '{}'", requestValidationError.get().getBody());
            return requestValidationError.get();
        }
        if (resources.allowNewExecution()) {
            int counter = resources.reserveResources();
            log.info("New request, total: {}, maxRequests: {}", counter, resources.getMaxRequests());
            
            ResponseEntity<Object> response;
            
            try {
                response = compileAndExecute(execution);
            } finally {
                resources.cleanup();
            }
            return response;
        }
        // The request has been throttled
        throttlingCounterMetric.increment();
        log.info("Request has been throttled, service reached maximum resources usage");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Request has been throttled, service reached maximum resources usage");
    }
    
    private ResponseEntity<Object> compileAndExecute(Execution execution) {
        // If the storage contains the id, it means we registered the url before and the client wants a push notification.
        if (hooksRepository.contains(execution.getId())) {
            log.info("Start long running execution, the result will be pushed to : {}", hooksRepository.get(execution.getId()));
            return longRunningCompilerService.execute(execution);
        }
        log.info("Start short running execution");
        return compilerService.execute(execution);
    }
    
    private Optional<ResponseEntity<Object>> validateRequest(Execution execution) {
        
        if (!checkFileName(execution.getSourceCodeFile().getOriginalFilename())) {
            return Optional.of(buildOutputError(
                    "Bad request, sourcecode file must match the following regex "
                            + WellKnownFiles.FILE_NAME_REGEX));
        }
        
        // sourcecode file format is correct, lets check the extension
        if (!checkFileExtension(execution.getSourceCodeFile().getOriginalFilename(), execution.getLanguage())) {
            return Optional.of(buildOutputError(
                    "Bad request, sourcecode file extension is not correct, it should be: "
                            + execution.getLanguage().getSourcecodeExtension()));
        }
    
        if (!checkFileName(execution.getExpectedOutputFile().getOriginalFilename())) {
            return Optional.of(buildOutputError(
                    "Bad request, expected output file must match the following regex "
                            + WellKnownFiles.FILE_NAME_REGEX));
        }
        
        MultipartFile inputFile = execution.getInputFile();
        
        // Input files can be null
        if (inputFile != null && !checkFileName(inputFile.getOriginalFilename())) {
            return Optional.of(buildOutputError(
                    "Bad request, input file must match the following regex "
                            + WellKnownFiles.FILE_NAME_REGEX));
        }
        
        if (execution.getTimeLimit() < minExecutionTime || execution.getTimeLimit() > maxExecutionTime) {
            String errorMessage = "Bad request, time limit must be between "
                    + minExecutionTime + " Sec and " + maxExecutionTime + " Sec, provided : "
                    + execution.getTimeLimit();
    
            return Optional.of(buildOutputError(errorMessage));
        }
    
        if (execution.getMemoryLimit() < minExecutionMemory || execution.getMemoryLimit() > maxExecutionMemory) {
            String errorMessage = "Bad request, memory limit must be between "
                    + minExecutionMemory + " MB and " + maxExecutionMemory + " MB, provided : "
                    + execution.getMemoryLimit();
    
            return Optional.of(buildOutputError(errorMessage));
        }
        
        return Optional.ofNullable(null);
    }
    
    private boolean checkFileExtension(String originalFilename, Language language) {
        return originalFilename.endsWith(language.getSourcecodeExtension());
    }
    
    private ResponseEntity buildOutputError(String errorMessage) {
        log.info(errorMessage);
        return ResponseEntity.badRequest()
                             .body(errorMessage);
    }
    
    /**
     * Checks for security reasons
     *
     * @param fileName
     * @return A boolean
     */
    private boolean checkFileName(String fileName) {
        return fileName != null && fileName.matches(WellKnownFiles.FILE_NAME_REGEX);
    }
}
