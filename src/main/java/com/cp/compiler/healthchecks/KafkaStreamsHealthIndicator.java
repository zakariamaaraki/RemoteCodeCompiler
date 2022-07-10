package com.cp.compiler.healthchecks;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

/**
 * The type Kafka streams health indicator.
 */
//Note that class name prefix before `HealthIndicator` will be camel-cased
//and used as a health component name, `kafkaStreams` here
@Profile("kafka")
@Component
public class KafkaStreamsHealthIndicator implements HealthIndicator {
    
    private final StreamsBuilderFactoryBean StreamsBuilderFactoryBean;
    
    /**
     * Instantiates a new Kafka streams health indicator.
     *
     * @param StreamsBuilderFactoryBean the streams builder factory bean
     */
    public KafkaStreamsHealthIndicator(StreamsBuilderFactoryBean StreamsBuilderFactoryBean) {
        super();
        this.StreamsBuilderFactoryBean = StreamsBuilderFactoryBean;
    }
    
    @Override
    public Health health() {
        KafkaStreams.State kafkaStreamsState = StreamsBuilderFactoryBean.getKafkaStreams().state();
        
        // CREATED, RUNNING or REBALANCING
        if (kafkaStreamsState == KafkaStreams.State.CREATED || kafkaStreamsState.isRunningOrRebalancing()) {
            //set details if you need one
            return Health.up().build();
        }
        
        // ERROR, NOT_RUNNING, PENDING_SHUTDOWN,
        return Health.down().withDetail("State", kafkaStreamsState.name()).build();
    }
}