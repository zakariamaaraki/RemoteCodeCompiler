package com.cp.compiler.controller;

import com.cp.compiler.model.Languages;
import com.cp.compiler.model.Response;
import com.cp.compiler.service.CompilerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/compiler")
public class CompilerController {
	
	@Autowired
	CompilerService compiler;
	
	// Python Compiler
	@RequestMapping(
			value = "python",
			method = RequestMethod.POST
	)
	@ApiOperation(
			value = "Python compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compile_python(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.Python);
	}
	
	// C Compiler
	@RequestMapping(
			value = "c",
			method = RequestMethod.POST
	)
	@ApiOperation(
			value = "C compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compile_c(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.C);
	}
	
	// C++ Compiler
	@RequestMapping(
			value = "cpp",
			method = RequestMethod.POST
	)
	@ApiOperation(
			value = "Cpp compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compile_cpp(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.Cpp);
	}
	
	// Java Compiler
	@RequestMapping(
			value = "java",
			method = RequestMethod.POST
	)
	@ApiOperation(
			value = "Java compiler",
			notes = "Provide outputFile, inputFile (not required), source code, time limit and memory limit",
			response = Response.class
	)
	public ResponseEntity<Object> compile_java(
			@ApiParam(value = "The expected output") @RequestPart(value = "outputFile", required = true) MultipartFile outputFile,
			@ApiParam(value = "Your source code") @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
			@ApiParam(value = "This one is not required, it's just the inputs") @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
			@ApiParam(value = "The time limit that the execution must not exceed") @RequestParam(value = "timeLimit", required = true) int timeLimit,
			@ApiParam(value = "The memory limit that the running program must not exceed") @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		return compiler.compile(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.Java);
	}
	
}
