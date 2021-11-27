package com.cp.compiler.controllers;

import com.cp.compiler.models.Language;
import com.cp.compiler.models.Response;
import com.cp.compiler.services.CompilerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Compiler Controller Class, this class exposes 4 endpoints for (Java, C, CPP, and Python)
 *
 * @author Zakaria Maaraki
 */

@RestController
@AllArgsConstructor
@RequestMapping("/compiler")
public class CompilerController {
	
	private CompilerService compiler;
	
	/**
	 * Python Compiler Controller
	 *
	 * @param outputFile  Expected output
	 * @param sourceCode  Python source code
	 * @param inputFile   Input data (optional)
	 * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
	 * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
	 * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Compilation Error, RunTime Error)
	 * @throws Exception
	 */
	@PostMapping("/python")
	@ApiOperation(
			value = "Python compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compilePython(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Language.PYTHON);
	}
	
	/**
	 * C Compiler Controller
	 *
	 * @param outputFile  Expected output
	 * @param sourceCode  C source code
	 * @param inputFile   Input data (optional)
	 * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
	 * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
	 * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded,
	 * Compilation Error, RunTime Error)
	 * @throws Exception
	 */
	@PostMapping("/c")
	@ApiOperation(
			value = "C compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compileC(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Language.C);
	}
	
	/**
	 * C++ Compiler Controller
	 *
	 * @param outputFile  Expected output
	 * @param sourceCode  C++ source code
	 * @param inputFile   Input data (optional)
	 * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
	 * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
	 * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded,
	 * Compilation Error, RunTime Error)
	 * @throws Exception
	 */
	@PostMapping("/cpp")
	@ApiOperation(
			value = "Cpp compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compileCpp(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Language.CPP);
	}
	
	/**
	 * Java Compiler Controller
	 *
	 * @param outputFile  Expected output
	 * @param sourceCode  Java source code
	 * @param inputFile   Input data (optional)
	 * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
	 * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
	 * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded,
	 * Compilation Error, RunTime Error)
	 * @throws Exception
	 */
	@PostMapping("/java")
	@ApiOperation(
			value = "Java compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compileJava(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Language.JAVA);
	}
	
}
