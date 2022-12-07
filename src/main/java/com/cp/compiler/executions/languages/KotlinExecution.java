package com.cp.compiler.executions.languages;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.Language;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.cp.compiler.wellknownconstants.WellKnownTemplates;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * The type Kotlin execution.
 */
@Getter
public class KotlinExecution extends Execution {
    
    /**
     * Instantiates a new Kotlin execution.
     *
     * @param sourceCode              the source code
     * @param testCases               the test cases
     * @param timeLimit               the time limit
     * @param memoryLimit             the memory limit
     * @param executionCounter        the execution counter
     * @param entryPointFileGenerator the entry point file generator
     */
    public KotlinExecution(MultipartFile sourceCode,
                           List<ConvertedTestCase> testCases,
                           int timeLimit,
                           int memoryLimit,
                           Counter executionCounter,
                           EntrypointFileGenerator entryPointFileGenerator) {
        super(sourceCode, testCases, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
    
    @SneakyThrows
    @Override
    public void createEntrypointFile(String inputFileName) {
        // This case is a bit different, Kotlin and Java files name must be the same as the name of the class
        // So we will keep the name of the file as it's sent by the user.
        val fileName = getSourceCodeFile().getOriginalFilename();
        val prefixName = fileName.substring(0, fileName.length() - 3); // remove .kt
        val commandPrefix = "kotlin " + prefixName;
        val executionCommand = inputFileName == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + inputFileName + "\n";
    
        val attributes = Map.of(
                "timeLimit", String.valueOf(getTimeLimit()),
                "memoryLimit", String.valueOf(getMemoryLimit()),
                "executionCommand", executionCommand);
    
        String content = getEntrypointFileGenerator()
                .createEntrypointFile(WellKnownTemplates.ENTRYPOINT_TEMPLATE, attributes);
    
        String path = getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME;
    
        Files.deleteIfExists(Path.of(path));
    
        try(OutputStream os = new FileOutputStream(path)) {
            os.write(content.getBytes(), 0, content.length());
        }
    }

    @Override
    public Language getLanguage() {
        return Language.KOTLIN;
    }
}
