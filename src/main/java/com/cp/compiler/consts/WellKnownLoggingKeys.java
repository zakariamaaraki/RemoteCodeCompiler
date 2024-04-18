package com.cp.compiler.consts;

/**
 * This class contains all constants related to logging
 *
 * @author Zakaria Maaraki
 */
public abstract class WellKnownLoggingKeys {
    
    private WellKnownLoggingKeys() {}
    
    /**
     * The constant PROGRAMMING_LANGUAGE.
     */
    public static final String PROGRAMMING_LANGUAGE = "compiler.language";
    
    /**
     * The constant IS_LONG_RUNNING.
     */
    public static final String IS_LONG_RUNNING = "compiler.is-long-running";
    
    /**
     * The constant USER_ID.
     */
    public static final String USER_ID = "compiler.user-id";
}
