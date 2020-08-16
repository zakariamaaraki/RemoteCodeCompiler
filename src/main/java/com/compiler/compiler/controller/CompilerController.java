package com.compiler.compiler.controller;

import com.compiler.compiler.model.Response;
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


@RestController
@RequestMapping("/")
public class CompilerController {
	
	Logger logger = LogManager.getLogger(CompilerController.class);
	
	@RequestMapping(
			value = "compile",
			method = RequestMethod.POST
	)
	public ResponseEntity<Object> compile(@RequestPart(value = "output", required = true) MultipartFile output,
	                                      @RequestPart(value = "sourceCode", required = true) MultipartFile sourceCode,
	                                      @RequestParam(value = "inputFile", required = false) MultipartFile inputFile,
	                                      @RequestParam(value = "timeLimit", required = true) int timeLimit,
	                                      @RequestParam(value = "memoryLimit", required = true) int memoryLimit
	) throws Exception {
		
		if(memoryLimit < 0 || memoryLimit > 1000)
			return ResponseEntity
					.badRequest()
					.body("Error memoryLimit must be between 0Mb and 1000Mb");
		
		if(timeLimit < 0 || timeLimit > 15)
			return ResponseEntity
					.badRequest()
					.body("Error timeLimit must be between 0 Sec and 15 Sec");
		
		LocalDateTime date = LocalDateTime.now();
		
		createEntrypointFile(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile);
		logger.info("entrypoint.sh file has been created");
		
		saveUploadedFiles(sourceCode, "utility/main.java");
		saveUploadedFiles(output, "utility/" + output.getOriginalFilename());
		if(inputFile != null)
			saveUploadedFiles(inputFile, "utility/" + inputFile.getOriginalFilename());
		logger.info("Files have been uploaded");
		
		logger.info("Building the docker image");
		String[] dockerCommand = new String[] {"docker", "image", "build", "utility", "-t", "remotecompiler"};
		ProcessBuilder probuilder = new ProcessBuilder(dockerCommand);
		Process process = probuilder.start();
		process.waitFor();
		logger.info("docker image has been built");
		
		logger.info("Running the container");
		dockerCommand = new String[] {"docker", "run", "-p", "8888:8888", "--rm", "remotecompiler"};
		probuilder = new ProcessBuilder(dockerCommand);
		process = probuilder.start();
		int status = process.waitFor();
		logger.info("End of the execution of the container");
		
		BufferedReader outputReader = new BufferedReader(new InputStreamReader(output.getInputStream()));
		StringBuilder outputBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder builder = new StringBuilder();
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
		
		String result = builder.toString();
		
		// delete files
		new File("utility/main.java").delete();
		new File("utility/" + output.getOriginalFilename()).delete();
		if(inputFile != null)
			new File("utility/" + inputFile.getOriginalFilename()).delete();
		logger.info("files have been deleted");
		
		String statusResponse = "";
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
		
		logger.info("Status response is " + statusResponse);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new Response(builder.toString(), outputBuilder.toString(), 0 , 0, statusResponse, ans, date));
		
	}
	
	// save file
	private void saveUploadedFiles(MultipartFile file, String name) throws IOException {
		if (file.isEmpty())
			return;
		byte[] bytes = file.getBytes();
		Path path = Paths.get(name);
		Files.write(path, bytes);
	}
	
	// create entrypoint.sh file
	private void createEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0,fileName.length() - 5) + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0,fileName.length() - 5) + " < " + inputFile.getOriginalFilename() + "\n";
		String content = "#!/usr/bin/env bash\n" +
				"mv main.java " + fileName+ "\n" +
				"javac " + fileName + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				 executionCommand +
				"exit $?\n";
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File("utility/entrypoint.sh"));
			os.write(content.getBytes(), 0, content.length());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
