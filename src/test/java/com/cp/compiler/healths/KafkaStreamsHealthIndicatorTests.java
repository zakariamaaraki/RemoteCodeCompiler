package com.cp.compiler.healths;

import com.cp.compiler.healthchecks.KafkaStreamsHealthIndicator;
import org.apache.kafka.streams.KafkaStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RunWith(MockitoJUnitRunner.class)
class KafkaStreamsHealthIndicatorTests {
    
    @Mock
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    
    @Mock
    private KafkaStreams kafkaStreams;
    
    @Test
    void shouldReturnHealthUpWhenKafkaStreamsIsInCreatedState() {
        // Given
        var healthIndicator = new KafkaStreamsHealthIndicator(streamsBuilderFactoryBean);
        Mockito.when(streamsBuilderFactoryBean.getKafkaStreams()).thenReturn(kafkaStreams);
        Mockito.when(kafkaStreams.state()).thenReturn(KafkaStreams.State.CREATED);
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.up().build().getStatus(), health.getStatus());
    }
    
    @Test
    void shouldReturnHealthUpWhenKafkaStreamsIsInRunningState() {
        // Given
        var healthIndicator = new KafkaStreamsHealthIndicator(streamsBuilderFactoryBean);
        Mockito.when(streamsBuilderFactoryBean.getKafkaStreams()).thenReturn(kafkaStreams);
        Mockito.when(kafkaStreams.state()).thenReturn(KafkaStreams.State.RUNNING);
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.up().build().getStatus(), health.getStatus());
    }
    
    @Test
    void shouldReturnHealthUpWhenKafkaStreamsIsInRebalancingState() {
        // Given
        var healthIndicator = new KafkaStreamsHealthIndicator(streamsBuilderFactoryBean);
        Mockito.when(streamsBuilderFactoryBean.getKafkaStreams()).thenReturn(kafkaStreams);
        Mockito.when(kafkaStreams.state()).thenReturn(KafkaStreams.State.REBALANCING);
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.up().build().getStatus(), health.getStatus());
    }
    
    @Test
    void shouldReturnHealthDownWhenKafkaStreamsIsInErrorState() {
        // Given
        var healthIndicator = new KafkaStreamsHealthIndicator(streamsBuilderFactoryBean);
        Mockito.when(streamsBuilderFactoryBean.getKafkaStreams()).thenReturn(kafkaStreams);
        Mockito.when(kafkaStreams.state()).thenReturn(KafkaStreams.State.ERROR);
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.down().build().getStatus(), health.getStatus());
    }
    
    @Test
    void shouldReturnHealthDownWhenKafkaStreamsIsInNotRunningState() {
        // Given
        var healthIndicator = new KafkaStreamsHealthIndicator(streamsBuilderFactoryBean);
        Mockito.when(streamsBuilderFactoryBean.getKafkaStreams()).thenReturn(kafkaStreams);
        Mockito.when(kafkaStreams.state()).thenReturn(KafkaStreams.State.NOT_RUNNING);
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.down().build().getStatus(), health.getStatus());
    }
    
    @Test
    void shouldReturnHealthDownWhenKafkaStreamsIsInPendingState() {
        // Given
        var healthIndicator = new KafkaStreamsHealthIndicator(streamsBuilderFactoryBean);
        Mockito.when(streamsBuilderFactoryBean.getKafkaStreams()).thenReturn(kafkaStreams);
        Mockito.when(kafkaStreams.state()).thenReturn(KafkaStreams.State.PENDING_SHUTDOWN);
        
        // When
        Health health = healthIndicator.health();
        
        // Then
        Assertions.assertEquals(Health.down().build().getStatus(), health.getStatus());
    }
}
