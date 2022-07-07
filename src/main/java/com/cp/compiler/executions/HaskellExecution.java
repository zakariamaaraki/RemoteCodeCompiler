package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.cp.compiler.wellknownconstants.WellKnownTemplates;
import com.cp.compiler.templates.EntrypointFileGenerator;
import com.cp.compiler.utils.StatusUtils;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * The type Haskell execution.
 */
@Getter
public class HaskellExecution extends Execution {
    
    /**
     * Instantiates a new Haskell execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public HaskellExecution(MultipartFile sourceCode,
                            MultipartFile inputFile,
                            MultipartFile expectedOutputFile,
                            int timeLimit,
                            int memoryLimit,
                            Counter executionCounter,
                            EntrypointFileGenerator entryPointFileGenerator) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter, entryPointFileGenerator);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        final var compiledFile = "main";
        final var commandPrefix = "./" + compiledFile;
        final String executionCommand;
        executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
        final var compilationCommand = Language.HASKELL.getCompilationCommand() + " -o " + compiledFile + " "
                + Language.HASKELL.getSourceCodeFileName();
    
        Map<String, String> attributes = Map.of(
                "rename", "false",
                "compile", "true",
                "fileName", Language.HASKELL.getSourceCodeFileName(),
                "defaultName", Language.HASKELL.getSourceCodeFileName(),
                "timeLimit", String.valueOf(getTimeLimit()),
                "compilationCommand", compilationCommand,
                "compilationErrorStatusCode", String.valueOf(StatusUtils.COMPILATION_ERROR_STATUS),
                "memoryLimit", String.valueOf(getMemoryLimit()),
                "executionCommand", executionCommand);
    
        String content = getEntrypointFileGenerator()
                .createEntrypointFile(WellKnownTemplates.ENTRYPOINT_TEMPLATE, attributes);
    
        try(OutputStream os = new FileOutputStream(getPath() + "/" + WellKnownFiles.ENTRYPOINT_FILE_NAME)) {
            os.write(content.getBytes(), 0, content.length());
        }
    }

    @Override
    public Language getLanguage() {
        return Language.HASKELL;
    }
}
