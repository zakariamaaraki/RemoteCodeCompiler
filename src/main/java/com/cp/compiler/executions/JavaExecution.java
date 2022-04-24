package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
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
     */
    public JavaExecution(MultipartFile sourceCode,
                         MultipartFile inputFile,
                         MultipartFile expectedOutputFile,
                         int timeLimit,
                         int memoryLimit) {
        super(sourceCode, inputFile, expectedOutputFile, timeLimit, memoryLimit);
        setpath(Language.JAVA);
    }
    
    @SneakyThrows
    @Override
    protected void createEntrypointFile() {
        var fileName = getSourceCode().getOriginalFilename();
        final var prefixName = fileName.substring(0, fileName.length() - 5);
        String executionCommand = getInputFile() == null
                ? TIMEOUT_CMD + getTimeLimit() + " java " + prefixName + "\n"
                : TIMEOUT_CMD + getTimeLimit() + " java " + prefixName + " < "
                + getInputFile().getOriginalFilename() + "\n";
        
        String content = BASH_HEADER +
                "mv main.java " + fileName + "\n"
                + Language.JAVA.getCommand() + " " + fileName + "\n"
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
        saveUploadedFiles(Language.JAVA);
    }
    
    @Override
    protected void copyDockerFileToExecutionDirectory() throws IOException {
        copyDockerFileToExecutionDirectory(Language.JAVA);
    }
}
