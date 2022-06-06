package com.cp.compiler.models;

/**
 * The type Well known commands.
 */
public abstract class WellKnownCommands {
    
    private WellKnownCommands() {}
    
    /**
     * The constant PYTHON_COMMAND_LINE.
     */
    public static final String PYTHON_COMMAND_LINE = "python3";
    
    /**
     * The constant C_COMMAND_LINE.
     */
    public static final String C_COMMAND_LINE = "gcc";
    
    /**
     * The constant CPP_COMMAND_LINE.
     */
    public static final String CPP_COMMAND_LINE = "g++";
    
    /**
     * The constant JAVA_COMMAND_LINE.
     */
    public static final String JAVA_COMMAND_LINE = "javac";
    
    /**
     * The constant GO_COMMAND_LINE.
     */
    public static final String GO_COMMAND_LINE = "go build";
    
    /**
     * The constant CS_COMMAND_LINE.
     */
    public static final String CS_COMMAND_LINE = "csc";
}
