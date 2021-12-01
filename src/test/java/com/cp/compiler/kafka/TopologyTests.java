package com.cp.compiler.kafka;

import com.cp.compiler.models.Language;
import com.cp.compiler.models.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

@ActiveProfiles("kafka")
@SpringBootTest
public class TopologyTests {
	
	@Autowired
	private Topology topology;
	
	private TopologyTestDriver streamTest;
	private TestInputTopic<String, String> inputTopic;
	
	private final Serde<String> stringSerde = new Serdes.StringSerde();
	
	// Expected output
	private File outputResource = new ClassPathResource("outputs/Test1.txt").getFile();
	private String expectedOutput = new String(Files.readAllBytes(outputResource.toPath()));
	
	// Java source code
	private File sourceCodeResource = new ClassPathResource("sources/java/Test1.java").getFile();
	private String sourceCode = new String(Files.readAllBytes(sourceCodeResource.toPath()));
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private Request request = new Request(null, expectedOutput, sourceCode, Language.JAVA, 10, 500);
	
	public TopologyTests() throws IOException {}
	
	@BeforeEach
	public void setUp() {
		
		// setup test driver
		final Properties props = new Properties();
		props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "compilerIdTest");
		props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:1234");
		props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		streamTest = new TopologyTestDriver(topology, props);
		
		// setup test topics
		inputTopic = streamTest.createInputTopic("kafka.topic.input", stringSerde.serializer(), stringSerde.serializer());
	}
}
