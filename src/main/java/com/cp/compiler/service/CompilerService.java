package com.cp.compiler.service;

import com.cp.compiler.model.Languages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CompilerService {
	
	public ResponseEntity<Object> compile(
			MultipartFile outputFile,
			MultipartFile sourceCode,
			MultipartFile inputFile,
			int timeLimit,
			int memoryLimit,
			Languages languages
	) throws Exception;
}
