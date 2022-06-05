package com.cp.compiler.repositories;

/**
 * The interface Hooks repository.
 */
public interface HooksRepository {
    
    /**
     * Get string.
     *
     * @param imageName the image name
     * @return the string
     */
    String get(String imageName);
    
    /**
     * Get and remove hook.
     *
     * @param imageName the image name
     * @return the url
     */
    String getAndRemove(String imageName);
    
    /**
     * Check if it contains a url.
     *
     * @param imageName the image name
     * @return the boolean
     */
    boolean contains(String imageName);
    
    /**
     * Add url.
     *
     * @param imageName the image name
     */
    void addUrl(String imageName, String url);
}
