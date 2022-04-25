package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
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
     */
    public CExecution(MultipartFile sourceCode,
                      MultipartFile inputFile,
                      MultipartFile expectedOutputFile,
                      int timeLimit,
                      int memoryLimit) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
        setpath(Language.C);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        String executionCommand = getInputFile() == null
                ? TIMEOUT_CMD + getTimeLimit() + " ./exec " + "\n"
                : TIMEOUT_CMD + getTimeLimit() + " ./exec " + " < " + getInputFile().getOriginalFilename() + "\n";
    
        String content = BASH_HEADER
                + Language.C.getCommand() + " main.c" + " -o exec" + "\n"
                + "ret=$?\n"
                + "if [ $ret -ne 0 ]\n"
                + "then\n"
                + "  exit 2\n"
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
        saveUploadedFiles(Language.C);
    }
    
    @Override
    protected void copyDockerFileToExecutionDirectory() throws IOException {
        copyDockerFileToExecutionDirectory(Language.C);
    }
}
