package com.cp.compiler.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Files util.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public class FilesUtil {
	
	private FilesUtil() {
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
	 * Delete file boolean.
	 *
	 * @param folder the folder where the file exists
	 * @param file   the filename that we want to delete
	 * @return boolean the file is deleted or not
	 */
	public static boolean deleteFile(String folder, String file) {
		if (folder != null && file != null) {
			String filePath = folder + "/" + file;
			return new File(filePath).delete();
		}
		return false;
	}
}
