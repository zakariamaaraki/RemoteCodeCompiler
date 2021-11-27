package com.cp.compiler.utilities;

import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The type Entry point file tests.
 */
class EntryPointFileTests {
	
	/**
	 * Should create python entry point file.
	 */
	@Test
	void shouldCreatePythonEntryPointFile() {
		// When
		EntryPointFile.createPythonEntrypointFile(10, 5, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(Language.PYTHON.getFolder() + "/entrypoint.sh")));
		FilesUtil.deleteFile(Language.PYTHON.getFolder(), "entrypoint.sh");
	}
	
	/**
	 * Should create java entry point file.
	 */
	@Test
	void shouldCreateJavaEntryPointFile() {
		// When
		EntryPointFile.createJavaEntrypointFile("test.java", 9, 4, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(Language.JAVA.getFolder()+ "/entrypoint.sh")));
		FilesUtil.deleteFile(Language.JAVA.getFolder(), "entrypoint.sh");
	}
	
	/**
	 * Should create c entry point file.
	 */
	@Test
	void shouldCreateCEntryPointFile() {
		// When
		EntryPointFile.createCEntrypointFile(8, 500, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(Language.C.getFolder() + "/entrypoint.sh")));
		FilesUtil.deleteFile(Language.C.getFolder(), "entrypoint.sh");
	}
	
	/**
	 * Should create cpp entry point file.
	 */
	@Test
	void shouldCreateCPPEntryPointFile() {
		// When
		EntryPointFile.createCppEntrypointFile(11, 400, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(Language.CPP.getFolder() + "/entrypoint.sh")));
		FilesUtil.deleteFile(Language.CPP.getFolder(), "entrypoint.sh");
	}
}
