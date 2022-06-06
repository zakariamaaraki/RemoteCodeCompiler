package com.cp.compiler.executions.java;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Language;
import com.cp.compiler.utilities.StatusUtil;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The type Java execution.
 */
@Getter
public class JavaExecution extends Execution {
    
    /**
     * Instantiates a new Java execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public JavaExecution(MultipartFile sourceCode,
                         MultipartFile inputFile,
                         MultipartFile expectedOutputFile,
                         int timeLimit,
                         int memoryLimit,
                         Counter executionCounter) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
        setpath(Language.JAVA);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        // This case is a bit different, Java file name must be the same as the name of the class
        // So we will keep the name of the file as it's sent by the user.
        var fileName = getSourceCodeFile().getOriginalFilename();
        final var prefixName = fileName.substring(0, fileName.length() - 5);
        final var commandPrefix = TIMEOUT_CMD + getTimeLimit() + " java " + prefixName;
        final var executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
        
        final var content = BASH_HEADER +
                "mv main.java " + fileName + "\n"
                + Language.JAVA.getCommand() + " " + fileName + " > /dev/null\n"
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
        saveUploadedFiles(Language.JAVA);
    }
    
    @Override
    protected void copyDockerFileToExecutionDirectory() throws IOException {
        copyDockerFileToExecutionDirectory(Language.JAVA);
    }
}
