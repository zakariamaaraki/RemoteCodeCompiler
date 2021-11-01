package com.cp.compiler.service;

import com.cp.compiler.model.Result;
import org.springframework.web.multipart.MultipartFile;

public interface ContainService {
	
	int buildImage(String folder, String imageName);
	
	Result runCode(String folder, String imageName, MultipartFile outputFile);
	
}
