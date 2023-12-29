package com.cp.compiler.models.testcases;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Converted test case.
 * Used to convert String into files used by the container.
 *
 * @author Zakaria Maaraki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransformedTestCase {

    private String testCaseId;
    
    private MultipartFile inputFile;

    private String expectedOutput;
    
    /**
     * Free memory space.
     * Set attributes to null and wait for GC to clean
     */
    public void freeMemorySpace() {
        inputFile = null;
        expectedOutput = null;
    }
}
