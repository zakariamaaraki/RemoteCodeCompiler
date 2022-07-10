package com.cp.compiler.kafka;

import com.cp.compiler.models.Response;
import com.cp.compiler.models.Result;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.services.CompilerService;

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
import java.util.Properties;

@ActiveProfiles("kafka")
@EmbeddedKafka(bootstrapServersProperty = "localhost:9092")
@DirtiesContext
@SpringBootTest
public class TopologyTests {
    
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
    public void shouldConsumeMessageFromInputTopicAndProduceMessageToOutputTopic() {

        // Given
        String jsonRequest = "{\n\"expectedOutput\": \"0\\n1\\n2\\n3\\n4\\n5\\n6\\n7\\n8\\n9\\n\",\n\"sourceCode\": " +
                "\"public class Test1 {\\npublic static void main(String[] args) {\\nint i = 0;\\nwhile (i < 10) " +
                "{\\nSystem.out.println(i++);\\n}}}\",\n\"language\": \"JAVA\",\"timeLimit\": 15,\"memoryLimit\": 500\n}";
        
        Mockito.when(compilerService.compile(Mockito.any()))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new Response(
                                new Result(
                                        Verdict.ACCEPTED,
                                        "test output",
                                        "",
                                        "test expected output",
                                        0),
                                LocalDateTime.now())));
                
        // When
        inputTopic.pipeInput(jsonRequest);
        
        // Then
        Assertions.assertThat(!outputTopic.isEmpty());
    }
    
    @Test
    public void ifInputMessageIsNotAValidRequestShouldPublishNullValueToOutputTopic() {
        
        // Given
        String jsonRequest = "This is a non valid json";
        
        // When
        inputTopic.pipeInput(jsonRequest);
        
        // Then
        Assertions.assertThat(!outputTopic.isEmpty());
        Assertions.assertThat(outputTopic.readValue() == null);
    }
}
