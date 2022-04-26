package com.cp.compiler.streams.transformers;

import com.cp.compiler.exceptions.ThrottlingException;
import com.cp.compiler.mappers.JsonMapper;
import com.cp.compiler.services.CompilerService;
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
    
    /**
     * Instantiates a new Compiler transformer.
     *
     * @param compilerService the compiler service
     * @param retryDuration   the retry duration
     */
    public CompilerTransformer(CompilerService compilerService, long throttlingDuration) {
        this.compilerService = compilerService;
        this.throttlingDuration = throttlingDuration;
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
            log.info("Request throttled {}, retrying after {}", throttlingException, throttlingDuration);
            return retryAfter(jsonRequest);
        } catch (Exception e) {
            log.error("Error : {}", e);
            return null;
        }
    }
    
    private String retryAfter(String jsonRequest) throws InterruptedException {
        Thread.sleep(throttlingDuration);
        return transform(jsonRequest);
    }
    
    @Override
    public void close() {
        // empty
    }
}
