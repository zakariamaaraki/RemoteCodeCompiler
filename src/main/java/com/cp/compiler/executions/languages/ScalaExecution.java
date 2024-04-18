package com.cp.compiler.executions.languages;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.Language;
import com.cp.compiler.consts.WellKnownFiles;
import com.cp.compiler.consts.WellKnownTemplates;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * The type Kotlin execution.
 *
 * @author Zakaria Maaraki
 */
@Getter
public class ScalaExecution extends Execution {
    
    /**
     * Instantiates a new Scala execution.
     *
     * @param sourceCode  the source code
     * @param testCases   the test cases
     * @param timeLimit   the time limit
     * @param memoryLimit the memory limit
     */
    public ScalaExecution(MultipartFile sourceCode,
                          List<TransformedTestCase> testCases,
                          int timeLimit,
                          int memoryLimit) {
        super(sourceCode, testCases, timeLimit, memoryLimit, ExecutionFactory.getExecutionType(Language.SCALA));
    }
    
    @Override
    public Map<String, String> getParameters(String inputFileName) {
        // This case is a bit different, Kotlin, Scala and Java files name must be the same as the name of the class
        // So we will keep the name of the file as it's sent by the user.
        val fileName = getSourceCodeFile().getOriginalFilename();
        val prefixName = fileName.substring(0, fileName.length() - 6); // remove .scala
        val commandPrefix = "scala " + prefixName;
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
    
    /**
     * Create an entrypoint file based on input file name and test case id
     * Note: template used for scala is different from the one used by other languages
     *
     * @param inputFileName the input file name
     * @param testCaseId    the test case id
     */
    @Override
    @SneakyThrows
    public void createEntrypointFile(String inputFileName, String testCaseId) {
        
        String content = getEntrypointFileGenerator()
                .createEntrypointFile(WellKnownTemplates.SCALA_ENTRYPOINT_TEMPLATE, getParameters(inputFileName));
        
        String path = getPath()
                + "/"
                + WellKnownFiles.ENTRYPOINT_FILE_NAME_PREFIX
                + testCaseId
                + WellKnownFiles.ENTRYPOINT_FILE_EXTENSION;
        
        Files.deleteIfExists(Path.of(path));
        
        try(OutputStream os = new FileOutputStream(path)) {
            os.write(content.getBytes(), 0, content.length());
        }
    }

    @Override
    public Language getLanguage() {
        return Language.SCALA;
    }
}
