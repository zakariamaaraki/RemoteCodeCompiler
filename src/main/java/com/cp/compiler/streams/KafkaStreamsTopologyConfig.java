package com.cp.compiler.streams;

import com.cp.compiler.wellknownconstants.WellKnownMetrics;
import com.cp.compiler.services.CompilerService;
import com.cp.compiler.streams.transformers.CompilerTransformer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueTransformerSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import javax.annotation.PostConstruct;

/**
 * The type Kafka streams topology config.
 *
 * @author Zakaria Maaraki
 */
@Profile("kafka")
@Slf4j
@Configuration
@EnableKafkaStreams
public class KafkaStreamsTopologyConfig {
    
    private final Serde<String> stringSerde = Serdes.String();
    
    private final MeterRegistry meterRegistry;
    
    private Counter throttlingRetriesCounter;
    
    /**
     * Instantiates a new Kafka streams topology config.
     *
     * @param meterRegistry the meter registry
     */
    public KafkaStreamsTopologyConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        throttlingRetriesCounter = meterRegistry.counter(WellKnownMetrics.KAFKA_THROTTLING_RETRIES, "broker", "kafka");
    }
    
    /**
     * Topology topology.
     *
     * @param inputTopic         the input topic
     * @param outputTopic        the output topic
     * @param throttlingDuration the throttling duration
     * @param builder            the topology builder
     * @param compilerService    the compiler service
     * @return the topology
     */
    @Bean
    public Topology topology(@Value("${spring.kafka.topics.input-topic}") String inputTopic,
                             @Value("${spring.kafka.topics.output-topic}") String outputTopic,
                             @Value("${spring.kafka.throttling-duration}") long throttlingDuration,
                             @Autowired StreamsBuilder builder,
                             @Qualifier("proxy") @Autowired CompilerService compilerService) {
        
        builder.stream(inputTopic, Consumed.with(stringSerde, stringSerde))
                .transformValues((ValueTransformerSupplier) () -> {
                    return new CompilerTransformer(compilerService, throttlingDuration, throttlingRetriesCounter);
                })
                .to(outputTopic, Produced.with(stringSerde, stringSerde));
    
        Topology topology = builder.build();
        log.info("Topology: {}", topology.describe());
        
        return topology;
    }
}
