package com.cp.compiler.executions.cs;

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
 * The type C# execution.
 */
@Getter
public class CSExecution extends Execution {
    
    /**
     * Instantiates a new C# execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public CSExecution(MultipartFile sourceCode,
                       MultipartFile inputFile,
                       MultipartFile expectedOutputFile,
                       int timeLimit,
                       int memoryLimit,
                       Counter executionCounter,
                       EntrypointFileGenerator entryPointFileGenerator) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, Language.CS, executionCounter, entryPointFileGenerator);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        final var commandPrefix = "mono main.exe";
        final String executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
    
        Map<String, String> attributes = Map.of(
                "rename", "false",
                "compile", "true",
                "defaultName", "main.cs",
                "fileName", "main.cs",
                "timeLimit", String.valueOf(getTimeLimit()),
                "compilationCommand", Language.CS.getCompilationCommand() + " " + Language.CS.getSourceCodeFileName(),
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
