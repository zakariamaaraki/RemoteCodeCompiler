package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import com.cp.compiler.utilities.StatusUtil;
import io.micrometer.core.instrument.Counter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The type Go execution.
 */
public class GoExecution extends  Execution {
    
    /**
     * Instantiates a new Go Execution.
     *
     * @param sourceCodeFile     the source code
     * @param inputFile          the inputFile file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    protected GoExecution(MultipartFile sourceCodeFile,
                          MultipartFile inputFile,
                          MultipartFile expectedOutputFile,
                          int timeLimit,
                          int memoryLimit,
                          Counter executionCounter) {
        super(sourceCodeFile, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
        setpath(Language.GO);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        String executionCommand = getInputFile() == null
                ? TIMEOUT_CMD + getTimeLimit() + " ./exec " + "\n"
                : TIMEOUT_CMD + getTimeLimit() + " ./exec " + " < " + getInputFile().getOriginalFilename() + "\n";
    
        String content = BASH_HEADER
                + Language.GO.getCommand() + " -o exec" + " main.go" + "\n"
                + "ret=$?\n"
                + "if [ $ret -ne 0 ]\n"
                + "then\n"
                + "  exit " + StatusUtil.COMPILATION_ERROR_STATUS + "\n"
                + "fi\n"
                + "ulimit -s " + getMemoryLimit() + "\n"
                + executionCommand
                + "exit $?\n";
    
        try(OutputStream os = new FileOutputStream(path + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
    
    @Override
    protected void saveUploadedFiles() throws IOException {
        saveUploadedFiles(Language.GO);
    }
    
    @Override
    protected void copyDockerFileToExecutionDirectory() throws IOException {
        copyDockerFileToExecutionDirectory(Language.GO);
    }
}
