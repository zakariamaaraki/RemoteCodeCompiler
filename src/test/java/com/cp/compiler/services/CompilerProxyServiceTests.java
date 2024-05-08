package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerBadRequestException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.Language;
import com.cp.compiler.repositories.hooks.HooksRepository;
import com.cp.compiler.services.api.CompilerProxy;
import com.cp.compiler.services.businesslogic.CompilerServiceDefault;
import com.cp.compiler.services.businesslogic.LongRunningCompilerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ActiveProfiles("longRunning")
@DirtiesContext
@SpringBootTest
class CompilerProxyServiceTests {
    
    private static final int BAD_REQUEST = 400;
    
    @Autowired
    private CompilerProxy compilerProxy;
    
    @MockBean
    private CompilerServiceDefault compilerServiceDefault;
    
    @MockBean
    private LongRunningCompilerService longRunningCompilerService;
    
    @Autowired
    private HooksRepository hooksRepository;
    
    private MultipartFile invalidFileName = new MockMultipartFile(
            "test",
            "",
            null,
            (byte[]) null);
    
    private MultipartFile validFileName = new MockMultipartFile(
            "test.java",
            "test.java",
            null,
            (byte[]) null);
    
    @Test
    void extensionsInLanguageEnumAreCorrect() {
        for (Language language : Language.values()) {
            String expectedExtension = "." + language.getDefaultSourcecodeFileName().split("\\.")[1];
            Assertions.assertEquals(expectedExtension, language.getSourcecodeExtension());
        }
    }
    
    @Test
    void shouldThrowCompilerBadRequestIfTheExtensionIsNotValid() {
        // Given
        MultipartFile invalidExtension = new MockMultipartFile(
                "test.c",
                "test.c",
                null,
                (byte[]) null);
    
        var testCase = new TransformedTestCase("id", validFileName, "test");
        
        Execution execution =
                ExecutionFactory.createExecution(invalidExtension, List.of(testCase), 10, 500, Language.JAVA);
    
        // When / Then
        Assertions.assertThrows(CompilerBadRequestException.class, () -> {
            compilerProxy.execute(execution);
        });
    }
    
    @Test
    void shouldThrowCompilerBadRequestIfNumberOfTestCasesIsZero() {
        // Given
        MultipartFile invalidExtension = new MockMultipartFile(
                "test.c",
                "test.c",
                null,
                (byte[]) null);
        
        Execution execution =
                ExecutionFactory.createExecution(invalidExtension, List.of(), 10, 500, Language.C);
    
        // When / Then
        Assertions.assertThrows(CompilerBadRequestException.class, () -> {
            compilerProxy.execute(execution);
        });
    }
    
    @Test
    void WhenSourceCodeFileNameIsInvalidShouldThrowCompilerBadRequest() {
        // Given
        var testCase = new TransformedTestCase("id", validFileName, "test");
        Execution execution =
                ExecutionFactory.createExecution(invalidFileName, List.of(testCase), 10, 500, Language.JAVA);
        
        // When / Then
        Assertions.assertThrows(CompilerBadRequestException.class, () -> {
            compilerProxy.execute(execution);
        });
    }
    
    @Test
    void shouldCallLongRunningOperation() {
        // Given
        var testCase = new TransformedTestCase("id", null, "test");
        Execution execution =
                ExecutionFactory.createExecution(validFileName, List.of(testCase), 10, 500, Language.JAVA);
    
        hooksRepository.addUrl(execution.getId(), "http://localhost");
        
        // When
        compilerProxy.execute(execution);
    
        // Then
        Mockito.verify(longRunningCompilerService, Mockito.times(1)).execute(execution);
    }
    
    @Test
    void shouldCallDefaultCompiler() {
        // Given
        var testCase = new TransformedTestCase("id", null, "test");
        Execution execution =
                ExecutionFactory.createExecution(validFileName, List.of(testCase), 10, 500, Language.JAVA);
        
        // When
        compilerProxy.execute(execution);
        
        // Then
        Mockito.verify(compilerServiceDefault, Mockito.times(1)).execute(execution);
    }
}
