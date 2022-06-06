package com.cp.compiler.executions.python;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Language;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The type Python execution.
 */
@Getter
public class PythonExecution extends Execution {
    
    /**
     * Instantiates a new Python execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public PythonExecution(MultipartFile sourceCode,
                           MultipartFile inputFile,
                           MultipartFile expectedOutputFile,
                           int timeLimit,
                           int memoryLimit,
                           Counter executionCounter) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
        setpath(Language.PYTHON);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        final var commandPrefix =
                TIMEOUT_CMD + getTimeLimit() + "s " + Language.PYTHON.getCommand() + " " + Language.PYTHON.getFile();
        final var executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
    
        final var content = BASH_HEADER
                + "ulimit -s " + getMemoryLimit() + "\n"
                + executionCommand
                + "exit $?\n";
    
        try(OutputStream os = new FileOutputStream(getPath() + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
    
    @Override
    protected void saveUploadedFiles() throws IOException {
        saveUploadedFiles(Language.PYTHON);
    }
    
    @Override
    protected void copyDockerFileToExecutionDirectory() throws IOException {
        copyDockerFileToExecutionDirectory(Language.PYTHON);
    }
}
