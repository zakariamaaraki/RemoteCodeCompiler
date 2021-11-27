package com.cp.compiler.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The type Files util tests.
 */
@ExtendWith(SpringExtension.class)
class FilesUtilTests {
	
	/**
	 * The Temp dir.
	 */
	@TempDir
	Path tempDir;
	
	/**
	 * Should delete file.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void shouldDeleteFile() throws IOException {
		// Given
		Path path = tempDir.resolve("test.txt");
		Files.write(path, "test".getBytes());
		
		// When
		FilesUtil.deleteFile(path.getParent().toString(), "test.txt");
		
		// Then
		Assertions.assertFalse(Files.exists(path));
	}
	
	/**
	 * Should return true after file deletion.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void shouldReturnTrueAfterFileDeletion() throws IOException {
		// Given
		Path path = tempDir.resolve("test.txt");
		Files.write(path, "test".getBytes());
		
		// When
		boolean isDeleted = FilesUtil.deleteFile(path.getParent().toString(), "test.txt");
		
		// Then
		Assertions.assertTrue(isDeleted);
	}
	
	/**
	 * Should return false if file is null.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void shouldReturnFalseIfFileIsNull() throws IOException {
		// Given
		Path path = tempDir.resolve("test.txt");
		Files.write(path, "test".getBytes());
		
		// When
		boolean isDeleted = FilesUtil.deleteFile(path.getParent().toString(), null);
		
		// Then
		Assertions.assertFalse(isDeleted);
	}
	
	/**
	 * Should return false if folder is null.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void shouldReturnFalseIfFolderIsNull() throws IOException {
		// Given
		Path path = tempDir.resolve("test.txt");
		Files.write(path, "test".getBytes());
		
		// When
		boolean isDeleted = FilesUtil.deleteFile(null, "test.txt");
		
		// Then
		Assertions.assertFalse(isDeleted);
	}
	
	/**
	 * Should save file.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void shouldSaveFile() throws IOException {
		// Given
		Path path = tempDir.resolve("test.txt");
		Files.write(path, "test".getBytes());
		MultipartFile multipartFile = new MockMultipartFile("test.txt", new FileInputStream(path.toFile()));
		
		// When
		FilesUtil.saveUploadedFiles(multipartFile, path.getParent() + "/test2.txt");
		
		// Then
		Assertions.assertTrue(Files.exists(Path.of(path.getParent() + "/test2.txt")));
	}
}
