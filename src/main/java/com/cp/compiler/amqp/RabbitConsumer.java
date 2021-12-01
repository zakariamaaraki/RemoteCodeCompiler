package com.cp.compiler.amqp;

import com.cp.compiler.mappers.JsonMapper;
import com.cp.compiler.services.CompilerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private CompilerService compilerService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${spring.rabbitmq.queues.output}")
	private String outputQueue;
	
	/**
	 * Listen.
	 *
	 * @param jsonRequest the json request
	 */
	@RabbitListener(queues = "${spring.rabbitmq.queues.input}")
	public void listen(String jsonRequest) {
		try {
			String jsonResult = JsonMapper.transform(jsonRequest, compilerService);
			rabbitTemplate.convertAndSend(outputQueue, jsonResult);
		} catch (Exception e) {
			log.error("Error : ", e);
		}
	}

}
