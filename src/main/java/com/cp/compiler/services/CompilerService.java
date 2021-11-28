package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerServerException;
import com.cp.compiler.models.Language;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Compiler service.
 *
 * @author: Zakaria Maaraki
 */
public interface CompilerService {
	
	/**
	 * Compile response entity.
	 *
	 * @param outputFile  the expected output result written in a file
	 * @param sourceCode  the source code to be compiled / interpreted and executed
	 * @param inputFile   the input file which is optional (can be null)
	 * @param timeLimit   the expected time limit must be between (0 and 15 sec)
	 * @param memoryLimit the expected memory limit must be between (0 and 1000 mb)
	 * @param language    the programming language
	 * @return a ResponseEntity containing the result of the execution
	 * @throws CompilerServerException error from the server
	 */
	ResponseEntity<Object> compile(MultipartFile outputFile, MultipartFile sourceCode, MultipartFile inputFile,
	                               int timeLimit, int memoryLimit, Language language) throws CompilerServerException;
	
	/**
	 * Gets max execution memory.
	 *
	 * @return the max execution memory
	 */
	int getMaxExecutionMemory();
	
	/**
	 * Gets min execution memory.
	 *
	 * @return the min execution memory
	 */
	int getMinExecutionMemory();
	
	/**
	 * Gets max execution time.
	 *
	 * @return the max execution time
	 */
	int getMaxExecutionTime();
	
	/**
	 * Gets min execution time.
	 *
	 * @return the min execution time
	 */
	int getMinExecutionTime();
}
