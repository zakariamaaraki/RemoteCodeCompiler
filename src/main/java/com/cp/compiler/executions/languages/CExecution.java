package com.cp.compiler.executions.languages;

import com.cp.compiler.executions.Execution;
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
        val commandPrefix = "./exec";
        val executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
    
        val attributes = Map.of(
                "timeLimit", String.valueOf(getTimeLimit()),
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
