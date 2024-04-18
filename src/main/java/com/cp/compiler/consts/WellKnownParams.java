package com.cp.compiler.consts;

/**
 * This class contains all constants related to request body parameters.
 *
 * @author Zakaria Maaraki
 */
public abstract class WellKnownParams {
    
    private WellKnownParams() {}
    
    /**
     * The constant URL.
     */
    public static final String URL = "url";
    
    /**
     * The constant PREFER.
     */
    public static final String PREFER = "prefer";
    
    /**
     * The constant USER_ID.
     */
    public static final String USER_ID = "userId";
    
    /**
     * The constant INPUTS.
     */
    public static final String INPUTS = "inputs";
    
    /**
     * The constant EXPECTED_OUTPUTS.
     */
    public static final String EXPECTED_OUTPUTS = "expectedOutputs";
    
    /**
     * The constant SOURCE_CODE.
     */
    public static final String SOURCE_CODE = "sourcecode";
    
    /**
     * The constant TIME_LIMIT.
     */
    public static final String TIME_LIMIT = "timeLimit";
    
    /**
     * The constant MEMORY_LIMIT.
     */
    public static final String MEMORY_LIMIT = "memoryLimit";
    
    /**
     * The constant LANGUAGE.
     */
    public static final String LANGUAGE = "language";
}
