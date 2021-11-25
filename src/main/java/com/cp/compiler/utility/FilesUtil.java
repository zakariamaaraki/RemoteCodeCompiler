package com.cp.compiler.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FilesUtil {
	
	private FilesUtil() {
	}
	
	/**
	 *
	 * @param file the file that we want to save locally
	 * @param name the path where the file will be saved
	 * @throws IOException
	 */
	public static void saveUploadedFiles(MultipartFile file, String name) throws IOException {
		if (file.isEmpty())
			return;
		byte[] bytes = file.getBytes();
		Path path = Paths.get(name);
		Files.write(path, bytes);
	}
	
	/**
	 *
	 * @param folder the folder where the file exists
	 * @param file the filename that we want to delete
	 * @return
	 */
	public static boolean deleteFile(String folder, String file) {
		if (folder != null && file != null) {
			String fileName = folder + "/" + file;
			new File(fileName).delete();
			return true;
		}
		return false;
	}
}
