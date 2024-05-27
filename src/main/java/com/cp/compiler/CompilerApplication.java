package com.cp.compiler;

import com.cp.compiler.api.filters.RateLimitingFilter;
import com.cp.compiler.services.platform.ratelimiting.UserBucketService;
import com.cp.compiler.services.platform.ratelimiting.UserBucketServiceDefault;
import com.cp.compiler.services.platform.resources.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Compiler application.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class CompilerApplication implements CommandLineRunner {
    
    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;

    @Value("${compiler.rate.limit:5}") // default capacity
    private int defaultRateLimitingCapacity;
    
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
        log.info("DELETE_DOCKER_IMAGE is set to: {}", deleteDockerImage);
        log.info("Cpus per execution = {}", resources.getMaxCpus());
        log.info("Rate limiting set to {} requests per second", defaultRateLimitingCapacity);
    }

    @Bean
    public UserBucketService userBucketService() {
        // x requests capacity, x request per second leak rate
        // With a cache size of 100 users.
        return new UserBucketServiceDefault(
                defaultRateLimitingCapacity,
                defaultRateLimitingCapacity * 1000,
                100);
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter(UserBucketService userBucketService) {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter(userBucketService));
        registrationBean.addUrlPatterns("/api/*"); // Apply to specific URL patterns
        return registrationBean;
    }
}
