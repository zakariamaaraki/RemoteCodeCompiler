package com.cp.compiler.models.testcases;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * The type Converted test case.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertedTestCase {

    private String testCaseId;
    
    private MultipartFile inputFile;

    private MultipartFile expectedOutputFile;
    
    /**
     * Free memory space.
     * Set attributes to null and wait for GC to clean
     */
    public void freeMemorySpace() {
        inputFile = null;
        expectedOutputFile = null;
    }
}
