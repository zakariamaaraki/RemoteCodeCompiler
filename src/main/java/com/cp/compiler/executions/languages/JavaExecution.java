package com.cp.compiler.executions.languages;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionType;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import com.cp.compiler.utils.FileUtils;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The type Java execution.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@Getter
public class JavaExecution extends Execution {
    
    /**
     * Instantiates a new Java execution.
     *
     * @param sourceCode    the source code
     * @param testCases     the test cases
     * @param timeLimit     the time limit
     * @param memoryLimit   the memory limit
     * @param executionType the execution type
     */
    public JavaExecution(MultipartFile sourceCode,
                         List<ConvertedTestCase> testCases,
                         int timeLimit,
                         int memoryLimit,
                         ExecutionType executionType) {
        super(sourceCode, testCases, timeLimit, memoryLimit, executionType);
    }
    
    @Override
    public Map<String, String> getParameters(String inputFileName) {
        // This case is a bit different, Java file name must be the same as the name of the class
        // So we will keep the name of the file as it's sent by the user.
        val fileName = getSourceCodeFile().getOriginalFilename();
        val prefixName = fileName.substring(0, fileName.length() - 5); // remove ".java"
        val commandPrefix = "java -Djava.security.manager -Djava.security.policy=./security.policy " + prefixName;
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
        log.debug("Copying Java security policy");
        copySecurityPolicyToExecutionDirectory();
    }
    
    /**
     * Copy security policy to execution directory.
     *
     * @throws IOException the io exception
     */
    private void copySecurityPolicyToExecutionDirectory() throws IOException {
        // Security policy
        FileUtils.copyFile(getLanguage()
                                .getFolderName()
                                .concat("/" + WellKnownFiles.JAVA_SECURITY_POLICY_FILE_NAME),
                           getPath().concat("/" + WellKnownFiles.JAVA_SECURITY_POLICY_FILE_NAME));
    }

    @Override
    public Language getLanguage() {
        return Language.JAVA;
    }
}
