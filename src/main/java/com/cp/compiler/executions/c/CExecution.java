package com.cp.compiler.executions.c;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.Language;
import com.cp.compiler.utilities.StatusUtil;
import io.micrometer.core.instrument.Counter;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * The type C execution.
 */
@Getter
public class CExecution extends Execution {
    
    /**
     * Instantiates a new C execution.
     *
     * @param sourceCode         the source code
     * @param inputFile          the input file
     * @param expectedOutputFile the expected output file
     * @param timeLimit          the time limit
     * @param memoryLimit        the memory limit
     * @param executionCounter   the execution counter
     */
    public CExecution(MultipartFile sourceCode,
                      MultipartFile inputFile,
                      MultipartFile expectedOutputFile,
                      int timeLimit,
                      int memoryLimit,
                      Counter executionCounter) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit, Language.C, executionCounter);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        final var commandPrefix = TIMEOUT_CMD + getTimeLimit() + " ./exec ";
        final var executionCommand = getInputFile() == null
                ? commandPrefix + "\n"
                : commandPrefix + " < " + getInputFile().getOriginalFilename() + "\n";
    
        final var content = BASH_HEADER
                + Language.C.getCommand() + " " + Language.C.getFile() + " -o exec" + " 1> /dev/null\n"
                + "ret=$?\n"
                + "if [ $ret -ne 0 ]\n"
                + "then\n"
                + "  exit " + StatusUtil.COMPILATION_ERROR_STATUS + "\n"
                + "fi\n"
                + "ulimit -s " + getMemoryLimit() + "\n"
                + executionCommand
                + "exit $?\n";
    
        try(OutputStream os = new FileOutputStream(getPath() + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
}
