package com.cp.compiler.kafka;

import com.cp.compiler.models.Language;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.testcases.TestCaseResult;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.services.businesslogic.CompilerService;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
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

@ActiveProfiles("kafka")
@EmbeddedKafka(bootstrapServersProperty = "localhost:9092")
@DirtiesContext
@SpringBootTest
class TopologyTests {
    
    @Autowired
    private Topology topology;
    
    @MockBean(name = "client")
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
        streamTest = new TopologyTestDriver(topology, props);
        
        // setup test topics
        inputTopic = streamTest.createInputTopic("kafka.topic.input",
                                                 stringSerde.serializer(),
                                                 stringSerde.serializer());
        
        outputTopic = streamTest.createOutputTopic("kafka.topic.output",
                                                   stringSerde.deserializer(),
                                                   stringSerde.deserializer());
    }
    
    @AfterEach
    public void tearDown() {
        streamTest.close();
    }
    
    @Test
    void shouldConsumeMessageFromInputTopicAndProduceMessageToOutputTopic() {

        // Given
        String jsonRequest = "{}";
    
        var testCaseResult = new TestCaseResult(Verdict.ACCEPTED,
                "test output",
                "",
                "test expected output",
                0);
        
        var response = new Response(
                Verdict.ACCEPTED.getStatusResponse(),
                Verdict.ACCEPTED.getStatusCode(),
                "",
                new LinkedHashMap<String, TestCaseResult>() {{
                    put("test1", testCaseResult);
                }},
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        
        Mockito.when(compilerService.execute(Mockito.any()))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(response));
                
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
