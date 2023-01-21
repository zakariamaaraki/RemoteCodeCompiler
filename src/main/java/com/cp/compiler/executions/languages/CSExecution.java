package com.cp.compiler.executions.languages;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The type C# execution.
 */
@Getter
public class CSExecution extends Execution {
    
    /**
     * Instantiates a new C# execution.
     *
     * @param sourceCode              the source code
     * @param testCases               the test cases
     * @param timeLimit               the time limit
     * @param memoryLimit             the memory limit
     * @param executionCounter        the execution counter
     * @param entryPointFileGenerator the entry point file generator
     */
    public CSExecution(MultipartFile sourceCode,
                       List<ConvertedTestCase> testCases,
                       int timeLimit,
                       int memoryLimit,
                       Counter executionCounter,
                       EntrypointFileGenerator entryPointFileGenerator) {
        super(sourceCode, testCases, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
    
    @Override
    public Map<String, String> getParameters(String inputFileName) {
        val commandPrefix = "mono main.exe";
        val executionCommand = inputFileName == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + inputFileName + "\n";
    
        return Map.of(
                "timeLimit", String.valueOf(getTimeLimit()),
                "memoryLimit", String.valueOf(getMemoryLimit()),
                "executionCommand", executionCommand);
    }
    
    @Override
    protected void copyLanguageSpecificFilesToExecutionDirectory() throws IOException {
        // Empty
    }
    
    @Override
    public Language getLanguage() {
        return Language.CS;
    }
}
