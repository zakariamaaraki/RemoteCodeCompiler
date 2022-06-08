package com.cp.compiler.executions.kotlin;

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
 * The type Kotlin execution.
 */
@Getter
public class KotlinExecution extends Execution {
    
    /**
     * Instantiates a new Kotlin execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public KotlinExecution(MultipartFile sourceCode,
                           MultipartFile inputFile,
                           MultipartFile expectedOutputFile,
                           int timeLimit,
                           int memoryLimit,
                           Counter executionCounter) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, executionCounter);
        setpath(Language.KOTLIN);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        // This case is a bit different, Kotlin and Java files name must be the same as the name of the class
        // So we will keep the name of the file as it's sent by the user.
        var fileName = getSourceCodeFile().getOriginalFilename();
        final var prefixName = fileName.substring(0, fileName.length() - 3);
        final var commandPrefix = TIMEOUT_CMD + getTimeLimit() + " kotlin " + prefixName;
        final var executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
        
        final var content = BASH_HEADER
                + "mv main.kt " + fileName + "\n"
                + Language.KOTLIN.getCommand() + " " + fileName + " 1> /dev/null\n"
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
        saveUploadedFiles(Language.KOTLIN);
    }
    
    @Override
    protected void copyDockerFileToExecutionDirectory() throws IOException {
        copyDockerFileToExecutionDirectory(Language.KOTLIN);
    }
}
