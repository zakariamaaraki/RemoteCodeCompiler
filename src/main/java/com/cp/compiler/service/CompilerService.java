package com.cp.compiler.service;

import com.cp.compiler.model.Languages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CompilerService {
	
	/**
	 *
	 * @param outputFile the expected output result written in a file
	 * @param sourceCode the source code to be compiled / interpreted and executed
	 * @param inputFile the input file which is optional (can be null)
	 * @param timeLimit the expected time limit must be between (0 and 15 sec)
	 * @param memoryLimit the expected memory limit must be between (0 and 1000 mb)
	 * @param languages the programming language
	 * @return a ResponseEntity containing the result of the execution
	 * @throws Exception
	 */
	ResponseEntity<Object> compile(MultipartFile outputFile, MultipartFile sourceCode, MultipartFile inputFile,
	                               int timeLimit, int memoryLimit, Languages languages) throws Exception;
}
