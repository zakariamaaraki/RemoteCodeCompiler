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
import java.nio.file.Paths;

/**
 * The type Files util tests.
 */
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
    
    /***
     * Should copy a file from src to dest, the dest path must exist
     *
     * @throws IOException
     */
    @Test
    void shouldCopyFileFromSrcToDest() throws IOException {
        // Given
        String src = "test.txt";
        Path path = tempDir.resolve(src);
        String dest = path.getParent() + "/test2.txt";
        Files.write(path, "test file".getBytes());
        MultipartFile multipartFile = new MockMultipartFile(src, new FileInputStream(path.toFile()));
        FilesUtil.saveUploadedFiles(multipartFile, path.getParent() + "/" + src);
        
        // When
        FilesUtil.copyFile(path.getParent() + "/" + src, dest);
        
        // Then
        Assertions.assertTrue(Files.exists(Path.of(dest)));
    }
}
