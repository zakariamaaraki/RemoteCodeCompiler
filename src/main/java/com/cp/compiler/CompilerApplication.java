package com.cp.compiler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CompilerApplication implements CommandLineRunner {


  @Value("${compiler.docker.image.delete:true}")
  private boolean deleteDockerImage;

  public static void main(String[] args) {
    SpringApplication.run(CompilerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("Env variables set to DELETE_DOCKER_IMAGE={}", deleteDockerImage);
  }
}
