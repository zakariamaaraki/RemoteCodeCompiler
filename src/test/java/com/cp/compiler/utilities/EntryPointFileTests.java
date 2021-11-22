package com.cp.compiler.utilities;

import com.cp.compiler.utility.EntryPointFile;
import com.cp.compiler.utility.FilesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The type Entry point file tests.
 */
class EntryPointFileTests {

  private static final String PYTHON_DIRECTORY = "utility_py";
  private static final String JAVA_DIRECTORY = "utility";
  private static final String C_DIRECTORY = "utility_c";
  private static final String CPP_DIRECTORY = "utility_cpp";


  /**
   * Should create python entry point file.
   */
  @Test
  void shouldCreatePythonEntryPointFile() {
    // When
    EntryPointFile.createPythonEntrypointFile(10, 5, null);

    // Then
    Assertions.assertTrue(Files.exists(Path.of(PYTHON_DIRECTORY + "/entrypoint.sh")));
    FilesUtil.deleteFile(PYTHON_DIRECTORY, "entrypoint.sh");
  }

  /**
   * Should create java entry point file.
   */
  @Test
  void shouldCreateJavaEntryPointFile() {
    // When
    EntryPointFile.createJavaEntrypointFile("test.java", 9, 4, null);

    // Then
    Assertions.assertTrue(Files.exists(Path.of(JAVA_DIRECTORY + "/entrypoint.sh")));
    FilesUtil.deleteFile(JAVA_DIRECTORY, "entrypoint.sh");
  }

  /**
   * Should create c entry point file.
   */
  @Test
  void shouldCreateCEntryPointFile() {
    // When
    EntryPointFile.createCEntrypointFile(8, 500, null);

    // Then
    Assertions.assertTrue(Files.exists(Path.of(C_DIRECTORY + "/entrypoint.sh")));
    FilesUtil.deleteFile(C_DIRECTORY, "entrypoint.sh");
  }

  /**
   * Should create cpp entry point file.
   */
  @Test
  void shouldCreateCPPEntryPointFile() {
    // When
    EntryPointFile.createCppEntrypointFile(11, 400, null);

    // Then
    Assertions.assertTrue(Files.exists(Path.of(CPP_DIRECTORY + "/entrypoint.sh")));
    FilesUtil.deleteFile(CPP_DIRECTORY, "entrypoint.sh");
  }
}
