package com.cp.compiler.executions.cpp;

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
 * The type Cpp execution.
 */
@Getter
public class CPPExecution extends Execution {
    
    /**
     * Instantiates a new Cpp execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public CPPExecution(MultipartFile sourceCode,
                        MultipartFile inputFile,
                        MultipartFile expectedOutputFile,
                        int timeLimit,
                        int memoryLimit,
                        Counter executionCounter,
                        EntrypointFileGenerator entryPointFileGenerator) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, Language.CPP, executionCounter, entryPointFileGenerator);
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
                "fileName", "main.cpp",
                "defaultName", "main.cpp",
                "timeLimit", String.valueOf(getTimeLimit()),
                "compilationCommand", Language.CPP.getCompilationCommand() + " " + Language.CPP.getSourceCodeFileName() + " -o exec",
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
