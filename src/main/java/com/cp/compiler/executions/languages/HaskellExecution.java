package com.cp.compiler.executions.languages;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionType;
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
 * The type Haskell execution.
 *
 * @author Zakaria Maaraki
 */
@Getter
public class HaskellExecution extends Execution {
    
    /**
     * Instantiates a new Haskell execution.
     *
     * @param sourceCode    the source code
     * @param testCases     the test cases
     * @param timeLimit     the time limit
     * @param memoryLimit   the memory limit
     * @param executionType the execution type
     */
    public HaskellExecution(MultipartFile sourceCode,
                            List<ConvertedTestCase> testCases,
                            int timeLimit,
                            int memoryLimit,
                            ExecutionType executionType) {
        super(sourceCode, testCases, timeLimit, memoryLimit, executionType);
    }
    
    @Override
    public Map<String, String> getParameters(String inputFileName) {
        val compiledFile = "main";
        val commandPrefix = "./" + compiledFile;
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
        return Language.HASKELL;
    }
}
