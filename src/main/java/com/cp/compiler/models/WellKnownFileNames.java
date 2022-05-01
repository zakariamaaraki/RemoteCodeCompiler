package com.cp.compiler.models;

/**
 * The type Well known file names.
 */
public abstract class WellKnownFileNames {
    
    private WellKnownFileNames() {}
    
    /**
     * The constant INPUT_FILE_NAME.
     */
    public static final String INPUT_FILE_NAME = "input.txt";
    
    /**
     * The constant EXPECTED_OUTPUT_FILE_NAME.
     */
    public static final String EXPECTED_OUTPUT_FILE_NAME = "expectedOutput.txt";
    
    /**
     * The constant FILE_NAME_REGEX.
     */
    public static final String FILE_NAME_REGEX = "^[a-zA-Z0-9_-]*\\.[a-zA-Z0-9_-]*$";
}
