package com.cp.compiler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The type Swagger config.
 *
 * @author: Zakaria Maaraki
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    
    private static final String TITLE = "Remote Code Compiler";
    
    private static final String DESCRIPTION = "An online code compiler supporting Java, Kotlin, Scala, C, C++, C#, " +
            "Golang, Python, Ruby, Rust and Haskell for competitive programming and coding interviews.";
    
    private static final String VERSION = "1.0.0";
    
    private static final String LICENCE = "GPL-3.0 Licence";
    
    private static final String LICENCE_URL = "https://github.com/zakariamaaraki/RemoteCodeCompiler/blob/master/LICENSE";
    
    /**
     * Api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cp.compiler.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }
    
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .version(VERSION)
                .license(LICENCE)
                .licenseUrl(LICENCE_URL)
                .build();
    }
}
