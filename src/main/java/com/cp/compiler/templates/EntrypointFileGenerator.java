package com.cp.compiler.templates;

import java.util.Map;

/**
 * The interface Entry point file generator.
 */
public interface EntrypointFileGenerator {
    
    /**
     * Generate an entrypoint file based on given attributes.
     *
     * @param templatePath the template path
     * @param attributes   the attributes
     * @return the content with given attributes
     */
    String createEntrypointFile(String templatePath, Map<String, String> attributes);
}
