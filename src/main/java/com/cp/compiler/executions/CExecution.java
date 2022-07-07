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
 * The type C execution.
 */
@Getter
public class CExecution extends Execution {

    /**
     * Instantiates a new Execution.
     *
     * @param sourceCodeFile          the source code
     * @param inputFile               the inputFile file
     * @param expectedOutputFile      the expected output file
     * @param timeLimit               the time limit
     * @param memoryLimit             the memory limit
     * @param executionCounter        the execution counter
     * @param entrypointFileGenerator the entrypointFile generator
     */
    public CExecution(MultipartFile sourceCodeFile,
                      MultipartFile inputFile,
                      MultipartFile expectedOutputFile,
                      int timeLimit,
                      int memoryLimit,
                      Counter executionCounter,
                      EntrypointFileGenerator entrypointFileGenerator) {
        super(sourceCodeFile, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter, entrypointFileGenerator);
    }

    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        final var commandPrefix = "./exec";
        final String executionCommand;
        executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
    
        Map<String, String> attributes = Map.of(
                "rename", "false",
                "compile", "true",
                "fileName", Language.C.getSourceCodeFileName(),
                "defaultName", Language.C.getSourceCodeFileName(),
                "timeLimit", String.valueOf(getTimeLimit()),
                "compilationCommand", Language.C.getCompilationCommand() + " " + Language.C.getSourceCodeFileName() + " -o exec",
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
        return Language.C;
    }
}
