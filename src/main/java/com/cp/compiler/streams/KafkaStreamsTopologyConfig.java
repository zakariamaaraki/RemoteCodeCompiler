package com.cp.compiler.streams;

import com.cp.compiler.services.CompilerService;
import com.cp.compiler.streams.transformers.CompilerTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueTransformerSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafkaStreams;

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
	
	/**
	 * The Compiler service.
	 */
	@Autowired
	private CompilerService compilerService;
	
	/**
	 * Topology topology.
	 *
	 * @param inputTopic  the input topic
	 * @param outputTopic the output topic
	 * @param builder     the topology builder
	 * @return the topology
	 */
	@Bean
	public Topology topology(@Value("${spring.kafka.topics.input-topic}") String inputTopic,
	                         @Value("${spring.kafka.topics.output-topic}") String outputTopic,
	                         @Autowired StreamsBuilder builder) {
		
		builder.stream(inputTopic, Consumed.with(stringSerde, stringSerde))
				.transformValues((ValueTransformerSupplier) () -> new CompilerTransformer(compilerService))
				.mapValues((v) -> {
					if (v == null) {
						return "Error from the server";
					}
					return v;
				})
				.to(outputTopic, Produced.with(stringSerde, stringSerde));
		
		return builder.build();
	}
}
