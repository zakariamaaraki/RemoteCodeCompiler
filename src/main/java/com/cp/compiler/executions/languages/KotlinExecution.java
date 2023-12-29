package com.cp.compiler.executions.languages;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.Language;
import lombok.Getter;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The type Kotlin execution.
 *
 * @author Zakaria Maaraki
 */
@Getter
public class KotlinExecution extends Execution {
    
    /**
     * Instantiates a new Kotlin execution.
     *
     * @param sourceCode  the source code
     * @param testCases   the test cases
     * @param timeLimit   the time limit
     * @param memoryLimit the memory limit
     */
    public KotlinExecution(MultipartFile sourceCode,
                           List<TransformedTestCase> testCases,
                           int timeLimit,
                           int memoryLimit) {
        super(sourceCode, testCases, timeLimit, memoryLimit, ExecutionFactory.getExecutionType(Language.KOTLIN));
    }
    
    @Override
    public Map<String, String> getParameters(String inputFileName) {
        // This case is a bit different, Kotlin and Java files name must be the same as the name of the class
        // So we will keep the name of the file as it's sent by the user.
        val fileName = getSourceCodeFile().getOriginalFilename();
        val prefixName = fileName.substring(0, fileName.length() - 3); // remove .kt
        val commandPrefix = "kotlin " + prefixName;
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
        return Language.KOTLIN;
    }
}
