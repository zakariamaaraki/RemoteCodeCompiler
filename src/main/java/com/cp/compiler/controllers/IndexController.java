package com.cp.compiler.controllers;

import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.Language;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;

@RestController
@RequestMapping("/")
public class IndexController {
    
    @ApiOperation(value = "Redirect to the index page", hidden = true)
    @GetMapping("/")
    public RedirectView redirectToIndexPage(RedirectAttributes attributes) {
        return new RedirectView("/swagger-ui.html");
    }
    
    @ApiOperation(value = "Get supported languages")
    @GetMapping("/languages")
    public Set<Language> getSupportedLanguages() {
        return ExecutionFactory.getRegisteredFactories();
    }
}
