package com.cp.compiler.streams.transformers;

import com.cp.compiler.exceptions.ThrottlingException;
import com.cp.compiler.mappers.JsonMapper;
import com.cp.compiler.services.CompilerService;
import io.micrometer.core.instrument.Counter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

/**
 * The type Compiler transformer.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public class CompilerTransformer implements ValueTransformer<String, String> {
    
    private CompilerService compilerService;
    
    private long throttlingDuration;
    
    private Counter throttlingRetriesCounter;
    
    /**
     * Instantiates a new Compiler transformer.
     *
     * @param compilerService          the compiler service
     * @param throttlingDuration       the throttling duration
     * @param throttlingRetriesCounter the throttling retries counter
     */
    public CompilerTransformer(CompilerService compilerService,
                               long throttlingDuration,
                               Counter throttlingRetriesCounter) {
        this.compilerService = compilerService;
        this.throttlingDuration = throttlingDuration;
        this.throttlingRetriesCounter = throttlingRetriesCounter;
    }
    
    @Override
    public void init(ProcessorContext processorContext) {
        // empty
    }
    
    @SneakyThrows
    @Override
    public String transform(String jsonRequest) {
        try {
            return JsonMapper.transform(jsonRequest, compilerService);
        } catch (ThrottlingException throttlingException) {
            log.info("Request has been throttled {}, retrying after {}", throttlingException, throttlingDuration);
            return retryAfter(jsonRequest);
        } catch (Exception e) {
            log.error("Error : {}", e);
            return null;
        }
    }
    
    private String retryAfter(String jsonRequest) throws InterruptedException {
        throttlingRetriesCounter.increment();
        Thread.sleep(throttlingDuration);
        return transform(jsonRequest);
    }
    
    @Override
    public void close() {
        // empty
    }
}
