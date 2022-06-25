package com.cp.compiler;

import com.cp.compiler.services.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private Resources resources;
    
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
        log.info("DELETE_DOCKER_IMAGE is set to : {}", deleteDockerImage);
        log.info("Cpus per execution = {}", resources.getMaxCpus());
    }
}
