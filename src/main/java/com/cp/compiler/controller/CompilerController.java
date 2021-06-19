package com.cp.compiler.controller;

import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.model.Response;
import com.cp.compiler.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;

import static com.cp.compiler.utility.EntryPointFile.*;

enum Languages {
	Python,
	C,
	Cpp,
	Java
}

@RestController
@RequestMapping("/compiler")
public class CompilerController {
	
	Logger logger = LogManager.getLogger(CompilerController.class);
	
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
		return compiler(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.Python);
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
		return compiler(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.C);
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
		return compiler(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.Cpp);
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
		return compiler(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, Languages.Java);
	}
	
	// save file
	private void saveUploadedFiles(MultipartFile file, String name) throws IOException {
		if (file.isEmpty())
			return;
		byte[] bytes = file.getBytes();
		Path path = Paths.get(name);
		Files.write(path, bytes);
	}
	
	// Compile method
	private ResponseEntity<Object> compiler(
			MultipartFile outputFile,
			MultipartFile sourceCode,
			MultipartFile inputFile,
			int timeLimit,
			int memoryLimit,
			Languages languages
	) throws Exception {
		String folder = "utility";
		String file = "main";
		if(languages == Languages.C) {
			folder += "_c";
			file += ".c";
		} else if(languages == Languages.Java) {
			file += ".java";
		} else if(languages == Languages.Cpp) {
			folder += "_cpp";
			file += ".cpp";
		} else {
			folder += "_py";
			file += ".py";
		}
		
		if(memoryLimit < 0 || memoryLimit > 1000)
			return ResponseEntity
					.badRequest()
					.body("Error memoryLimit must be between 0Mb and 1000Mb");
		
		if(timeLimit < 0 || timeLimit > 15)
			return ResponseEntity
					.badRequest()
					.body("Error timeLimit must be between 0 Sec and 15 Sec");
		
		LocalDateTime date = LocalDateTime.now();
		
		createEntrypointFile(sourceCode, inputFile, timeLimit, memoryLimit, languages);
		
		logger.info("entrypoint.sh file has been created");
		
		saveUploadedFiles(sourceCode, folder + "/" + file);
		saveUploadedFiles(outputFile, folder + "/" + outputFile.getOriginalFilename());
		if(inputFile != null)
			saveUploadedFiles(inputFile, folder + "/" + inputFile.getOriginalFilename());
		logger.info("Files has been uploaded");
		
		String imageName = "compile" + new Date().getTime();
		
		Result result;
		
		try {
			result = runCode(folder, imageName, outputFile);
		} catch(DockerBuildException exception) {
			
			// delete files
			deleteFile(folder, file);
			deleteFile(folder,outputFile.getOriginalFilename());
			if(inputFile != null)
				deleteFile(folder,inputFile.getOriginalFilename());
			
			throw exception;
		}
		
		
		String statusResponse = result.getVerdict();
		logger.info("Status response is " + statusResponse);
		
		// delete files
		deleteFile(folder, file);
		deleteFile(folder,outputFile.getOriginalFilename());
		if(inputFile != null)
			deleteFile(folder,inputFile.getOriginalFilename());
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new Response(result.getOutput(), result.getExpectedOutput(), statusResponse, date));
	}
	
	private int buildImage(String folder, String imageName) throws InterruptedException, IOException {
		String[] dockerCommand = new String[] {"docker", "image", "build", folder, "-t", imageName};
		ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
		Process process = processbuilder.start();
		return process.waitFor();
	}
	
	private Result runCode(String folder, String imageName, MultipartFile outputFile) throws InterruptedException, IOException {
		
		logger.info("Building the docker image");
		int status = buildImage(folder, imageName);
		
		if(status == 0)
			logger.info("Docker image has been built");
		else {
			throw new DockerBuildException("Error while building image");
		}
		
		logger.info("Running the container");
		String[] dockerCommand = new String[] {"docker", "run", "--rm", imageName};
		ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
		Process process = processbuilder.start();
		status = process.waitFor();
		logger.info("End of the execution of the container");
		
		BufferedReader outputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
		StringBuilder outputBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder builder = new StringBuilder();
		
		boolean result = compareResult(outputReader, outputBuilder, reader, builder);
		String statusResponse = statusResponse(status, result);
		
		return new Result(statusResponse, builder.toString(), outputBuilder.toString());
		
	}
	
	private boolean compareResult(BufferedReader outputReader, StringBuilder outputBuilder, BufferedReader reader, StringBuilder builder) throws IOException {
		String line = null;
		String outputLine = null;
		boolean ans = true;
		
		while ( (line = reader.readLine()) != null && (outputLine = outputReader.readLine()) != null) {
			if(!line.equals(outputLine))
				ans = false;
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
			
			outputBuilder.append(outputLine);
			outputBuilder.append(System.getProperty("line.separator"));
		}
		
		if(line != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		
		if(outputLine != null) {
			outputBuilder.append(outputLine);
			outputBuilder.append(System.getProperty("line.separator"));
		}
		
		while ( (line = reader.readLine()) != null) {
			ans = false;
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		
		while ( (outputLine = outputReader.readLine()) != null) {
			ans = false;
			outputBuilder.append(outputLine);
			outputBuilder.append(System.getProperty("line.separator"));
		}
		return ans;
	}
	
	private boolean deleteFile(String folder, String file) {
		if(folder != null && file != null) {
			String fileName = folder + "/" + file;
			new File(fileName).delete();
			logger.info("file " + fileName + " has been deleted");
			return true;
		}
		return false;
	}
	
	private String statusResponse(int status, boolean ans) {
		
		String statusResponse;
		if(status == 0) {
			if(ans)
				statusResponse = "Accepted";
			else
				statusResponse = "Wrong Answer";
		}
		else if(status == 2)
			statusResponse = "Compilation Error";
		else if(status == 1)
			statusResponse = "Runtime Error";
		else if(status == 139)
			statusResponse = "Out Of Memory";
		else
			statusResponse = "Time Limit Exceeded";
		return statusResponse;
	}
	
	private void createEntrypointFile(MultipartFile sourceCode, MultipartFile inputFile, int timeLimit, int memoryLimit, Languages languages) {
		if(languages == Languages.Java) {
			createJavaEntrypointFile(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile);
		} else if(languages == Languages.C) {
			createCEntrypointFile(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile);
		} else if(languages == Languages.Cpp) {
			createCppEntrypointFile(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile);
		} else {
			createPythonEntrypointFile(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile);
		}
	}
	
	
}
