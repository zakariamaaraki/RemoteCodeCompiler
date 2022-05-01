package com.cp.compiler.models;

/**
 * The type Well known urls.
 */
public abstract class WellKnownUrls {
    
    private WellKnownUrls() {}
    
    /**
     * The constant URL_REGEX.
     */
    public static final String URL_REGEX =  "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
}
