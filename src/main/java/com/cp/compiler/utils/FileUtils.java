package com.cp.compiler.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * The type Files util.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public abstract class FileUtils {
    
    private FileUtils() {
    }
    
    /**
     * Save uploaded files.
     *
     * @param file the file that we want to save locally
     * @param name the path where the file will be saved
     * @throws IOException the exception
     */
    public static void saveUploadedFiles(MultipartFile file, String name) throws IOException {
        if (file.isEmpty())
            return;
        byte[] bytes = file.getBytes();
        Path path = Paths.get(name);
        Files.write(path, bytes);
    }
    
    /**
     * Copy a File from src to dest
     *
     * @param src Original path
     * @param dest Copied path
     * @throws IOException
     */
    public static void copyFile(String src, String dest) throws IOException {
        Path copied = Paths.get(dest);
        Path originalPath = Paths.get(src);
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    }
}
