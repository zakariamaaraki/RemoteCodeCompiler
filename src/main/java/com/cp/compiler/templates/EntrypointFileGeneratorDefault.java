package com.cp.compiler.templates;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;

/**
 * The type Entry point file generator.
 */
@Component
@AllArgsConstructor
public class EntrypointFileGeneratorDefault implements EntrypointFileGenerator {
    
    private final SpringTemplateEngine templateEngine;
    
    @Override
    public String createEntrypointFile(String templatePath, Map<String, String> attributes) {
        Context context = new Context();
        context.setVariable("compiler", attributes);
        return templateEngine.process(templatePath, context);
    }
}
