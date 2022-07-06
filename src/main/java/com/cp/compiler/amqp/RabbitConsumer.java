package com.cp.compiler.amqp;

import com.cp.compiler.exceptions.ThrottlingException;
import com.cp.compiler.mappers.JsonMapper;
import com.cp.compiler.wellknownconstants.WellKnownMetrics;
import com.cp.compiler.services.CompilerService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The type Rabbit consumer.
 *
 * @author Zakaria Maaraki
 */
@Profile("rabbitmq")
@Slf4j
@Component
public class RabbitConsumer {
    
    @Qualifier("proxy")
    @Autowired
    private CompilerService compilerService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    @Value("${spring.rabbitmq.queues.output}")
    private String outputQueue;
    
    @Value("${spring.rabbitmq.throttling-duration}")
    private long throttlingDuration;
    
    private Counter throttlingRetriesCounter;
    
    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        throttlingRetriesCounter = meterRegistry.counter(WellKnownMetrics.AMQP_THROTTLING_RETRIES, "broker", "rabbitmq");
    }
    
    /**
     * Listen.
     *
     * @param jsonRequest the json request
     * @throws Exception the exception
     */
    @RabbitListener(queues = "${spring.rabbitmq.queues.input}")
    public void listen(String jsonRequest) {
        try {
            String jsonResult = transform(jsonRequest);
            rabbitTemplate.convertAndSend(outputQueue, jsonResult);
        } catch (Exception e) {
            log.error("Error : {}", e);
        }
    }
    
    private String transform(String jsonRequest) throws Exception {
        try {
            return JsonMapper.transform(jsonRequest, compilerService);
        } catch (ThrottlingException throttlingException) {
            log.info("Request throttled {}, retrying after {}", throttlingException, throttlingDuration);
            return retryAfter(jsonRequest);
        }
    }
    
    private String retryAfter(String jsonRequest) throws Exception {
        throttlingRetriesCounter.increment();
        Thread.sleep(throttlingDuration);
        return transform(jsonRequest);
    }
    
}
