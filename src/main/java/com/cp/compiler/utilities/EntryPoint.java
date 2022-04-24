package com.cp.compiler.utilities;

import com.cp.compiler.models.Language;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * The type Entry point file.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public class EntryPoint {
    
    private static final String TIMEOUT_CMD = "timeout --signal=SIGTERM ";
    private static final String BASH_HEADER = "#!/usr/bin/env bash\n";
    
    private EntryPoint() {
    }
    
    /**
     * Creates entrypoint file
     *
     * @param sourceCode The source code
     * @param inputFile The input file (can be null)
     * @param timeLimit The time limit
     * @param memoryLimit The memory limit
     * @param language The programming language
     * @param path The path where to store the entrypoint file
     */
    public static void createEntrypointFile(MultipartFile sourceCode,
                                            MultipartFile inputFile,
                                            int timeLimit,
                                            int memoryLimit,
                                            Language language,
                                            String path) {
        if (language == Language.JAVA) {
            // The name of the class should be equals to the name of the file
            createJavaEntrypointFile(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile, path);
        } else if (language == Language.C) {
            createCEntrypointFile(timeLimit, memoryLimit, inputFile, path);
        } else if (language == Language.CPP) {
            createCppEntrypointFile(timeLimit, memoryLimit, inputFile, path);
        } else {
            createPythonEntrypointFile(timeLimit, memoryLimit, inputFile, path);
        }
    }
    
    /**
     * Creates python entrypoint file.
     *
     * @param timeLimit   the expected time limit that execution must not exceed
     * @param memoryLimit the expected memory limit
     * @param inputFile   the input file that contains input data (can be null)
     */
    @SneakyThrows
    public static void createPythonEntrypointFile(int timeLimit,
                                                  int memoryLimit,
                                                  MultipartFile inputFile,
                                                  String path) {
        
        String executionCommand = inputFile == null
                ? TIMEOUT_CMD + timeLimit + "s " + Language.PYTHON.getCommand() + " main.py" + "\n"
                : TIMEOUT_CMD+ timeLimit + "s " + Language.PYTHON.getCommand()
                    + " main.py" + " < " + inputFile.getOriginalFilename() + "\n";
        
        String content = BASH_HEADER
                + "ulimit -s " + memoryLimit + "\n"
                + executionCommand
                + "exit $?\n";
        
        try(OutputStream os = new FileOutputStream(path + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
    
    /**
     * Creates java entrypoint file.
     *
     * @param fileName    the source code file name (in java a class public must have the same name as the file name)
     * @param timeLimit   the expected time limit that execution must not exceed
     * @param memoryLimit the expected memory limit
     * @param inputFile   the input file that contains input data (can be null)
     */
    @SneakyThrows
    public static void createJavaEntrypointFile(String fileName,
                                                int timeLimit,
                                                int memoryLimit,
                                                MultipartFile inputFile,
                                                String path) {
        
        final var prefixName = fileName.substring(0, fileName.length() - 5);
        String executionCommand = inputFile == null
                ? TIMEOUT_CMD + timeLimit + " java " + prefixName + "\n"
                : TIMEOUT_CMD + timeLimit + " java " + prefixName + " < "
                    + inputFile.getOriginalFilename() + "\n";
        
        String content = BASH_HEADER +
                "mv main.java " + fileName + "\n"
                + Language.JAVA.getCommand() + " " + fileName + "\n"
                + "ret=$?\n"
                + "if [ $ret -ne 0 ]\n"
                + "then\n"
                + "  exit 2\n"
                + "fi\n"
                + "ulimit -s " + memoryLimit + "\n"
                + executionCommand
                + "exit $?\n";

        try(OutputStream os = new FileOutputStream(path + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
    
    /**
     * Creates c entrypoint file.
     *
     * @param timeLimit   the expected time limit that execution must not exceed
     * @param memoryLimit the expected memory limit
     * @param inputFile   the input file that contains input data (can be null)
     */
    @SneakyThrows
    public static void createCEntrypointFile(int timeLimit, int memoryLimit, MultipartFile inputFile, String path) {
        
        String executionCommand = inputFile == null
                ? TIMEOUT_CMD + timeLimit + " ./exec " + "\n"
                : TIMEOUT_CMD + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
        
        String content = BASH_HEADER
                + Language.C.getCommand() + " main.c" + " -o exec" + "\n"
                + "ret=$?\n"
                + "if [ $ret -ne 0 ]\n"
                + "then\n"
                + "  exit 2\n"
                + "fi\n"
                + "ulimit -s " + memoryLimit + "\n"
                + executionCommand
                + "exit $?\n";
        
        try(OutputStream os = new FileOutputStream(path + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
    
    /**
     * Creates cpp entrypoint file.
     *
     * @param timeLimit   the expected time limit that execution must not exceed
     * @param memoryLimit the expected memory limit
     * @param inputFile   the input file that contains input data (can be null)
     */
    @SneakyThrows
    public static void createCppEntrypointFile(int timeLimit, int memoryLimit, MultipartFile inputFile, String path) {
        
        String executionCommand = inputFile == null
                ? TIMEOUT_CMD + timeLimit + " ./exec " + "\n"
                : TIMEOUT_CMD + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
        
        String content = BASH_HEADER
                + Language.CPP.getCommand() + " main.cpp" + " -o exec" + "\n"
                + "ret=$?\n"
                + "if [ $ret -ne 0 ]\n"
                + "then\n"
                + "  exit 2\n"
                + "fi\n"
                + "ulimit -s " + memoryLimit + "\n"
                + executionCommand
                + "exit $?\n";
        
        try(OutputStream os = new FileOutputStream(path + "/entrypoint.sh")) {
            os.write(content.getBytes(), 0, content.length());
        }
    }
    
}