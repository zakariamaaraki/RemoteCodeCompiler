package com.cp.compiler.templates;

import com.cp.compiler.consts.WellKnownTemplates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class EntrypointGeneratorTests {
    
    @Autowired
    private EntrypointFileGenerator entrypointFileGenerator;
    
    @Test
    void shouldParseAndGenerateAnEntrypointContent() {
        // Given
        Map<String, String> attributes = Map.of(
                "timeLimit", "10",
                "memoryLimit", "500",
                "executionCommand", "cmd");
    
        // When
        String content = entrypointFileGenerator
                .createEntrypointFile(WellKnownTemplates.ENTRYPOINT_TEMPLATE, attributes);
        
        // Then
        Assertions.assertNotNull(content);
    }
}
