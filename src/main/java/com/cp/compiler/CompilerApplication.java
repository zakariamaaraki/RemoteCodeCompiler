package com.cp.compiler;

import com.cp.compiler.executions.*;
import com.cp.compiler.models.Language;
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
    
    // Used programming languages
    static {
        ExecutionFactory.register(Language.JAVA, JavaExecutionFactory::new);
        ExecutionFactory.register(Language.PYTHON, PythonExecutionFactory::new);
        ExecutionFactory.register(Language.C, CExecutionFactory::new);
        ExecutionFactory.register(Language.CPP, CPPExecutionFactory::new);
    }
    
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
