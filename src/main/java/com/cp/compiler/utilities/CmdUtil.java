package com.cp.compiler.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The type Cmd util.
 *
 * @author Zakaria Maaraki
 */
public abstract class CmdUtil {
    
    private static final int MAX_ERROR_LENGTH = 200; // number of chars
    
    private CmdUtil() {}
    
    /**
     * Run cmd string.
     *
     * @param params the params
     * @return the string
     * @throws IOException the io exception
     */
    public static String runCmd(String... params) throws IOException {
        String[] dockerCommand = params;
        ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
        Process process = processbuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return readOutput(reader);
    }
    
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
}
