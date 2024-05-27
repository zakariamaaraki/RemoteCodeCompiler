package com.cp.compiler.kafka;

import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.contract.testcases.TestCaseResult;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.services.businesslogic.CompilerService;

import com.cp.compiler.streams.KafkaStreamsTopologyConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("kafka")
@EmbeddedKafka(bootstrapServersProperty = "localhost:9092")
@DirtiesContext
@ExtendWith(MockitoExtension.class)
class TopologyTests {
    private KafkaStreamsTopologyConfig topology;
    
    @Mock
    private CompilerService compilerService;

    private TopologyTestDriver streamTest;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;
    
    private final Serde<String> stringSerde = new Serdes.StringSerde();
    
    @BeforeEach
    public void setUp() {
        
        // setup test driver
        final Properties props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "compilerIdTest");
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        String inputTopicName = "kafka.topic.input";
        String outputTopicName = "kafka.topic.output";

        topology = new KafkaStreamsTopologyConfig(null);

        streamTest = new TopologyTestDriver(
                topology.topology(
                    inputTopicName,
                    outputTopicName,
                    1000,
                        new StreamsBuilder(),
                    compilerService),
                props);
        
        // setup test topics
        inputTopic = streamTest.createInputTopic(inputTopicName,
                                                 stringSerde.serializer(),
                                                 stringSerde.serializer());
        
        outputTopic = streamTest.createOutputTopic(outputTopicName,
                                                   stringSerde.deserializer(),
                                                   stringSerde.deserializer());

        var testCaseResult = new TestCaseResult(Verdict.ACCEPTED,
                "test output",
                "",
                "test expected output",
                0);

        var response = new RemoteCodeCompilerExecutionResponse(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<>() {{
                    put("test1", testCaseResult);
                }},
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());

        Mockito.lenient().when(compilerService.execute(any()))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new RemoteCodeCompilerResponse(response)));
    }
    
    @AfterEach
    public void tearDown() {
        streamTest.close();
    }
    
    @Test
    void shouldConsumeMessageFromInputTopicAndProduceMessageToOutputTopic() {

        // Given
        String jsonRequest = "{}";
                
        // When
        inputTopic.pipeInput(jsonRequest);
        
        // Then
        Assertions.assertThat(!outputTopic.isEmpty()).isTrue();
    }
    
    @Test
    void ifInputMessageIsNotAValidRequestShouldPublishNullValueToOutputTopic() {
        
        // Given
        String jsonRequest = "This is a non valid json";
        
        // When
        inputTopic.pipeInput(jsonRequest);
        
        // Then
        Assertions.assertThat(!outputTopic.isEmpty()).isTrue();
        Assertions.assertThat(outputTopic.readValue() == null).isTrue();
    }
}
