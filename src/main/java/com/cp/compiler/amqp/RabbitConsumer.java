package com.cp.compiler.amqp;

import com.cp.compiler.exceptions.ThrottlingException;
import com.cp.compiler.mappers.JsonMapper;
import com.cp.compiler.services.CompilerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
	
	@Value("${spring.rabbitmq.queues.output}")
	private String outputQueue;
	
	@Value("${spring.rabbitmq.throttling-duration}")
	private long throttlingDuration;
	
	/**
	 * Listen.
	 *
	 * @param jsonRequest the json request
	 * @throws Exception the exception
	 */
	@RabbitListener(queues = "${spring.rabbitmq.queues.input}")
	public void listen(String jsonRequest) throws Exception {
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
		Thread.sleep(throttlingDuration);
		return transform(jsonRequest);
	}
	
}
