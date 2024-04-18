package com.cp.compiler.api.controllers;

import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.contract.Language;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/debug")
public class DebugController {
    
    @ApiOperation(value = "Get supported languages")
    @GetMapping("/languages")
    public Set<Language> getSupportedLanguages() {
        return ExecutionFactory.getRegisteredFactories();
    }
}
