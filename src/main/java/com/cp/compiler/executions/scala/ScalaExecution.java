package com.cp.compiler.executions.scala;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.WellKnownTemplates;
import com.cp.compiler.templates.EntrypointFileGenerator;
import com.cp.compiler.utilities.StatusUtil;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * The type Kotlin execution.
 */
@Getter
public class ScalaExecution extends Execution {
    
    /**
     * Instantiates a new Scala execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public ScalaExecution(MultipartFile sourceCode,
                          MultipartFile inputFile,
                          MultipartFile expectedOutputFile,
                          int timeLimit,
                          int memoryLimit,
                          Counter executionCounter,
                          EntrypointFileGenerator entryPointFileGenerator) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, Language.SCALA, executionCounter, entryPointFileGenerator);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        // This case is a bit different, Kotlin, Scala and Java files name must be the same as the name of the class
        // So we will keep the name of the file as it's sent by the user.
        var fileName = getSourceCodeFile().getOriginalFilename();
        final var prefixName = fileName.substring(0, fileName.length() - 3); // remove .sc
        final var commandPrefix = "scala " + prefixName;
        final String executionCommand;
        executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
    
        Map<String, String> attributes = Map.of(
                "rename", "true",
                "compile", "true",
                "defaultName", "main.scala",
                "fileName", fileName,
                "timeLimit", String.valueOf(getTimeLimit()),
                "compilationCommand", Language.SCALA.getCompilationCommand() + " " + fileName,
                "compilationErrorStatusCode", String.valueOf(StatusUtil.COMPILATION_ERROR_STATUS),
                "memoryLimit", String.valueOf(getMemoryLimit()),
                "executionCommand", executionCommand);
    
        String content = getEntrypointFileGenerator()
                .createEntrypointFile(WellKnownTemplates.ENTRYPOINT_TEMPLATE, attributes);
    
        try(OutputStream os = new FileOutputStream(getPath() + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
}
