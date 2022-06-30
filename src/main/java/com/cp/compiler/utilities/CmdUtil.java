package com.cp.compiler.utilities;

import com.cp.compiler.exceptions.ProcessExecutionException;
import com.cp.compiler.models.ProcessOutput;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * The type Cmd util.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public abstract class CmdUtil {
    
    private static final int MAX_ERROR_LENGTH = 200; // number of chars
    
    private CmdUtil() {}
    
    /**
     * Read output string.
     *
     * @param reader the reader
     * @return the string
     * @throws IOException the io exception
     */
    public static String readOutput(BufferedReader reader) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder();
        
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        
        return builder.toString();
    }
    
    /**
     * Compare output boolean.
     *
     * @param output         the output
     * @param expectedOutput the expected output
     * @return the boolean
     */
    public static boolean compareOutput(String output, String expectedOutput) {
        // Remove \n and extra spaces
        return output
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("/n","")
                .equals(expectedOutput.trim()
                        .replaceAll("\\s+", " ")
                        .replaceAll("/n", ""));
    }
    
    /**
     * Build error output string.
     *
     * @param readOutput the read output
     * @return the string
     */
    public static String buildErrorOutput(String readOutput) {
        if (readOutput.length() > MAX_ERROR_LENGTH) {
            return readOutput.substring(0, MAX_ERROR_LENGTH) + "...";
        }
        return readOutput;
    }
    
    
    /**
     * Execute process process output.
     *
     * @param commands      the commands
     * @param timeout       the timeout
     * @param timeoutStatus the timeout status
     * @return the process output
     * @throws ProcessExecutionException the process execution exception
     */
    public static ProcessOutput executeProcess(String[] commands, long timeout, int timeoutStatus)
            throws ProcessExecutionException {
        try {
            ProcessBuilder processbuilder = new ProcessBuilder(commands);
            Process process = processbuilder.start();
            long executionStartTime = System.currentTimeMillis();
    
            // Do not let the process exceed the timeout
            process.waitFor(timeout, TimeUnit.MILLISECONDS);
            long executionEndTime = System.currentTimeMillis();
    
            int status = 0;
            String stdOut = "";
            String stdErr = "";
    
            // Check if the process process is alive,
            // if it's so then destroy it and return a timeout status
            if (process.isAlive()) {
                status = timeoutStatus;
                log.info("The process exceed the " + timeout + " Millis allowed for its execution");
                process.destroy();
                log.info("The process has been destroyed");
            } else {
                status = process.exitValue();
        
                BufferedReader containerOutputReader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));
                stdOut = CmdUtil.readOutput(containerOutputReader);
        
                BufferedReader containerErrorReader =
                        new BufferedReader(new InputStreamReader(process.getErrorStream()));
                stdErr = CmdUtil.buildErrorOutput(CmdUtil.readOutput(containerErrorReader));
            }
    
            return ProcessOutput
                    .builder()
                    .stdOut(stdOut)
                    .stdErr(stdErr)
                    .status(status)
                    .executionDuration(executionEndTime - executionStartTime)
                    .build();
        } catch(Exception e) {
            throw new ProcessExecutionException("Fatal error: " + e.getMessage());
        }
    }
}
