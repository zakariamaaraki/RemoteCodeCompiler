package com.cp.compiler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Compiler application.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@SpringBootApplication
public class CompilerApplication implements CommandLineRunner {
	
	
	@Value("${compiler.docker.image.delete:true}")
	private boolean deleteDockerImage;
	
	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CompilerApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("DELETE_DOCKER_IMAGE set to : {}", deleteDockerImage);
	}
}
