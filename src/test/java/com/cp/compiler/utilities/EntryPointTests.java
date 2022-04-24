package com.cp.compiler.utilities;

import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test entrypoint files creation
 */
class EntryPointTests {
    
    /**
     * Should create python entry point file.
     */
    @Test
    void shouldCreatePythonEntryPointFile() throws IOException {
        // When
        EntryPoint.createPythonEntrypointFile(10, 5, null, Language.PYTHON.getFolder());
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(Language.PYTHON.getFolder() + "/entrypoint.sh")));
        Files.delete(Path.of(Language.PYTHON.getFolder() + "/entrypoint.sh"));
    }
    
    /**
     * Should create java entry point file.
     */
    @Test
    void shouldCreateJavaEntryPointFile() throws IOException {
        // When
        EntryPoint.createJavaEntrypointFile("test.java",
                                                9,
                                                4,
                                                null,
                                                Language.JAVA.getFolder());
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(Language.JAVA.getFolder()+ "/entrypoint.sh")));
        Files.delete(Path.of(Language.JAVA.getFolder() + "/entrypoint.sh"));
    }
    
    /**
     * Should create c entry point file.
     */
    @Test
    void shouldCreateCEntryPointFile() throws IOException {
        // When
        EntryPoint.createCEntrypointFile(8, 500, null, Language.C.getFolder());
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(Language.C.getFolder() + "/entrypoint.sh")));
        Files.delete(Path.of(Language.C.getFolder() + "/entrypoint.sh"));
    }
    
    /**
     * Should create cpp entry point file.
     */
    @Test
    void shouldCreateCPPEntryPointFile() throws IOException {
        // When
        EntryPoint.createCppEntrypointFile(11, 400, null, Language.CPP.getFolder());
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(Language.CPP.getFolder() + "/entrypoint.sh")));
        Files.delete(Path.of(Language.CPP.getFolder() + "/entrypoint.sh"));
    }
}
