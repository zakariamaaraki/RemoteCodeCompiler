package com.cp.compiler.utils;

import com.cp.compiler.exceptions.ProcessExecutionException;
import com.cp.compiler.exceptions.ProcessExecutionTimeoutException;
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
public abstract class CmdUtils {

    private static final int MAX_ERROR_LENGTH = 200; // number of chars
    /**
     * The constant LONG_MESSAGE_TRAIL.
     */
    public static final String LONG_MESSAGE_TRAIL = "...";

    private CmdUtils() {}
    
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
        return trimText(output)
          .equals(trimText(expectedOutput));
    }

    /**
     * Remove extra white space and trailing carriage returns
     *
     * @param text
     * @return cleaned text
     */
    private static String trimText(String text) {
        return text
          .trim()
          .replaceAll("\\s+", " ")
          .replaceAll("/n","");
    }
    
    /**
     * Build error output string.
     *
     * @param readOutput the read output
     * @return the string
     */
    public static String buildErrorOutput(String readOutput) {
        if (readOutput.length() > MAX_ERROR_LENGTH) {
            return readOutput.substring(0, MAX_ERROR_LENGTH - LONG_MESSAGE_TRAIL.length()) + LONG_MESSAGE_TRAIL;
        }
        return readOutput;
    }
    
    
    /**
     * Execute process process output.
     *
     * @param commands the commands
     * @param timeout  the timeout
     * @return the process output
     * @throws ProcessExecutionException        the process execution exception
     * @throws ProcessExecutionTimeoutException the process execution timeout exception
     */
    public static ProcessOutput executeProcess(String[] commands, long timeout)
            throws ProcessExecutionException, ProcessExecutionTimeoutException {
        
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout should be a positive value");
        }
        
        if (commands == null || commands.length < 1) {
            throw new IllegalArgumentException("commands should have at least one element");
        }
        
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
    
            // Check if the process is alive,
            // if it's so then destroy it and return a timeout status
            if (process.isAlive()) {
                log.info("The process exceeded the {} Millis allowed for its execution", timeout);
                process.destroy();
                log.info("The process has been destroyed");
                throw new ProcessExecutionTimeoutException(timeout);
            } else {
                status = process.exitValue();
        
                BufferedReader containerOutputReader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));
                stdOut = CmdUtils.readOutput(containerOutputReader);
        
                BufferedReader containerErrorReader =
                        new BufferedReader(new InputStreamReader(process.getErrorStream()));
                stdErr = CmdUtils.buildErrorOutput(CmdUtils.readOutput(containerErrorReader));
            }
    
            return ProcessOutput
                    .builder()
                    .stdOut(stdOut)
                    .stdErr(stdErr)
                    .status(status)
                    .executionDuration(executionEndTime - executionStartTime)
                    .build();
            
        } catch(ProcessExecutionTimeoutException processExecutionTimeoutException) {
            throw processExecutionTimeoutException;
        } catch(Exception exception) {
            throw new ProcessExecutionException("Fatal error for command " + commands + " : " + exception.getMessage());
        }
    }
}
