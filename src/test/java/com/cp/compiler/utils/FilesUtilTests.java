package com.cp.compiler.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class FilesUtilTests {
    
    @TempDir
    Path tempDir;
    
    @Test
    void shouldSaveFile() throws IOException {
        // Given
        Path path = tempDir.resolve("test.txt");
        Files.write(path, "test".getBytes());
        MultipartFile multipartFile = new MockMultipartFile("test.txt", new FileInputStream(path.toFile()));
        
        // When
        FileUtils.saveUploadedFiles(multipartFile, path.getParent() + "/test2.txt");
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(path.getParent() + "/test2.txt")));
    }
    
    @Test
    void shouldCopyFileFromSrcToDest() throws IOException {
        // Given
        String src = "test.txt";
        Path path = tempDir.resolve(src);
        String dest = path.getParent() + "/test2.txt";
        Files.write(path, "test file".getBytes());
        MultipartFile multipartFile = new MockMultipartFile(src, new FileInputStream(path.toFile()));
        FileUtils.saveUploadedFiles(multipartFile, path.getParent() + "/" + src);
        
        // When
        FileUtils.copyFile(path.getParent() + "/" + src, dest);
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(dest)));
    }
}
