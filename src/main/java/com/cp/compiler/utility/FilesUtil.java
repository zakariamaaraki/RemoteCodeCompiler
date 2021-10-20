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
	
	private FilesUtil() {}
	
	// save file
	public static void saveUploadedFiles(MultipartFile file, String name) throws IOException {
		if (file.isEmpty())
			return;
		byte[] bytes = file.getBytes();
		Path path = Paths.get(name);
		Files.write(path, bytes);
	}
	
	// delete file
	public static boolean deleteFile(String folder, String file) {
		if(folder != null && file != null) {
			String fileName = folder + "/" + file;
			new File(fileName).delete();
			log.info("file " + fileName + " has been deleted");
			return true;
		}
		return false;
	}
}
