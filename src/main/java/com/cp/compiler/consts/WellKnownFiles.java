package com.cp.compiler.consts;

/**
 * This class contains all constants related to files
 *
 * @author Zakaria Maaraki
 */
public abstract class WellKnownFiles {
    
    private WellKnownFiles() {}
    
    /**
     * The constant JAVA_FILE_NAME.
     */
    public static final String JAVA_FILE_NAME = "main.java";
    
    /**
     * The constant PYTHON_FILE_NAME.
     */
    public static final String PYTHON_FILE_NAME = "main.py";
    
    /**
     * The constant C_FILE_NAME.
     */
    public static final String C_FILE_NAME = "main.c";
    
    /**
     * The constant CPP_FILE_NAME.
     */
    public static final String CPP_FILE_NAME = "main.cpp";
    
    /**
     * The constant GO_FILE_NAME.
     */
    public static final String GO_FILE_NAME = "main.go";
    
    /**
     * The constant CS_FILE_NAME.
     */
    public static final String CS_FILE_NAME = "main.cs";
    
    /**
     * The constant KOTLIN_FILE_NAME.
     */
    public static final String KOTLIN_FILE_NAME = "main.kt";
    
    /**
     * The constant SCALA_FILE_NAME.
     */
    public static final String SCALA_FILE_NAME = "main.scala";
    
    /**
     * The constant RUST_FILE_NAME.
     */
    public static final String RUST_FILE_NAME = "main.rs";
    
    /**
     * The constant RUBY_FILE_NAME.
     */
    public static final String RUBY_FILE_NAME = "main.rb";
    
    /**
     * The constant HASKELL_FILE_NAME.
     */
    public static final String HASKELL_FILE_NAME = "main.hs";
    
    /**
     * The constant JAVA_SECURITY_POLICY_FILE_NAME.
     */
    public static final String JAVA_SECURITY_POLICY_FILE_NAME = "security.policy";
    
    /**
     * The constant INPUT_FILE_NAME.
     */
    public static final String INPUT_FILE_NAME = "input.txt";
    
    /**
     * The constant EXPECTED_OUTPUT_FILE_NAME.
     */
    public static final String EXPECTED_OUTPUT_FILE_NAME = "expectedOutput.txt";
    
    /**
     * The constant ENTRYPOINT_FILE_NAME_PREFIX.
     */
    public static final String ENTRYPOINT_FILE_NAME_PREFIX = "entrypoint-";
    
    /**
     * The constant ENTRYPOINT_FILE_EXTENSION.
     */
    public static final String ENTRYPOINT_FILE_EXTENSION = ".sh";
    
    /**
     * The constant FILE_NAME_REGEX.
     */
    public static final String FILE_NAME_REGEX = "^[a-zA-Z0-9_-]*\\.[a-zA-Z0-9_-]*$";
    
    /**
     * The constant EXECUTION_DOCKERFILE_NAME.
     */
    public static final String EXECUTION_DOCKERFILE_NAME = "Dockerfile.execution";
}
