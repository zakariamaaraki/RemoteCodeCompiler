package com.cp.compiler;

import com.cp.compiler.utility.EntryPointFile;
import com.cp.compiler.utility.FilesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class EntryPointFileTests {
	
	private static final String PYTHON_DIRECTORY = "utility_py";
	private static final String JAVA_DIRECTORY = "utility";
	private static final String C_DIRECTORY = "utility_c";
	private static final String CPP_DIRECTORY = "utility_cpp";
	
	
	@Test
	public void shouldCreatePythonEntryPointFile() {
		// When
		EntryPointFile.createPythonEntrypointFile(10, 5, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(PYTHON_DIRECTORY + "/entrypoint.sh")));
		FilesUtil.deleteFile(PYTHON_DIRECTORY, "entrypoint.sh");
	}
	
	@Test
	public void shouldCreateJavaEntryPointFile() {
		// When
		EntryPointFile.createJavaEntrypointFile("test.java", 9, 4, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(JAVA_DIRECTORY + "/entrypoint.sh")));
		FilesUtil.deleteFile(JAVA_DIRECTORY, "entrypoint.sh");
	}
	
	@Test
	public void shouldCreateCEntryPointFile() {
		// When
		EntryPointFile.createCEntrypointFile(8, 500, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(C_DIRECTORY + "/entrypoint.sh")));
		FilesUtil.deleteFile(C_DIRECTORY, "entrypoint.sh");
	}
	
	@Test
	public void shouldCreateCPPEntryPointFile() {
		// When
		EntryPointFile.createCppEntrypointFile(11, 400, null);
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(CPP_DIRECTORY + "/entrypoint.sh")));
		FilesUtil.deleteFile(CPP_DIRECTORY, "entrypoint.sh");
	}
}
