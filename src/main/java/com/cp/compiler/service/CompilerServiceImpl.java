package com.cp.compiler.service;

import com.cp.compiler.model.Languages;
import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.model.Response;
import com.cp.compiler.model.Result;
import com.cp.compiler.utility.FilesUtil;
import com.cp.compiler.utility.StatusUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cp.compiler.utility.EntryPointFile.*;
import static com.cp.compiler.utility.EntryPointFile.createPythonEntrypointFile;

@Slf4j
@Service
public class CompilerServiceImpl implements CompilerService{
	
	@Autowired
	private MeterRegistry meterRegistry;
	
	private Timer buildTimer;
	private Timer runTimer;
	
	@PostConstruct
	public void init() {
		buildTimer = meterRegistry.timer("compiler", "container", "build");
		runTimer = meterRegistry.timer("compiler", "container", "run");
	}
	
	// Compile method
	@Override
	public ResponseEntity<Object> compile(
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
		
		log.info("entrypoint.sh file has been created");
		
		FilesUtil.saveUploadedFiles(sourceCode, folder + "/" + file);
		FilesUtil.saveUploadedFiles(outputFile, folder + "/" + outputFile.getOriginalFilename());
		if(inputFile != null)
			FilesUtil.saveUploadedFiles(inputFile, folder + "/" + inputFile.getOriginalFilename());
		log.info("Files have been uploaded");
		
		String imageName = "compile" + new Date().getTime();
		
		Result result;
		
		try {
			result = runCode(folder, imageName, outputFile);
		} catch(DockerBuildException exception) {
			
			// delete files
			FilesUtil.deleteFile(folder, file);
			FilesUtil.deleteFile(folder,outputFile.getOriginalFilename());
			if(inputFile != null)
				FilesUtil.deleteFile(folder,inputFile.getOriginalFilename());
			
			throw exception;
		}
		
		
		String statusResponse = result.getVerdict();
		log.info("Status response is " + statusResponse);
		
		// delete files
		FilesUtil.deleteFile(folder, file);
		FilesUtil.deleteFile(folder,outputFile.getOriginalFilename());
		if(inputFile != null)
			FilesUtil.deleteFile(folder,inputFile.getOriginalFilename());
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new Response(result.getOutput(), result.getExpectedOutput(), statusResponse, date));
	}
	
	private int buildImage(String folder, String imageName) {
		return buildTimer.record(() -> {
			try {
				String[] dockerCommand = new String[] {"docker", "image", "build", folder, "-t", imageName};
				ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
				Process process = processbuilder.start();
				return process.waitFor();
			} catch (Exception e) {
				log.error("Error : ", e);
				// Error during the build
				return 1;
			}
		});
	}
	
	private Result runCode(String folder, String imageName, MultipartFile outputFile) {
		log.info("Building the docker image");
		
		// variable used in lambda function should be atomic
		AtomicInteger status = new AtomicInteger(buildImage(folder, imageName));
		
		if(status.get() == 0)
			log.info("Docker image has been built");
		else {
			throw new DockerBuildException("Error while building image");
		}
		
		return runTimer.record(() -> {
			log.info("Running the container");
			String[] dockerCommand = new String[] {"docker", "run", "--rm", imageName};
			ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
			Process process;
			try {
				process = processbuilder.start();
				status.set(process.waitFor());
				log.info("End of the execution of the container");
				
				BufferedReader outputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
				StringBuilder outputBuilder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder builder = new StringBuilder();
				
				boolean result = compareResult(outputReader, outputBuilder, reader, builder);
				String statusResponse = StatusUtil.statusResponse(status.get(), result);
				return new Result(statusResponse, builder.toString(), outputBuilder.toString());
			} catch (Exception e) {
				log.error("Error : ", e);
				return new Result(StatusUtil.statusResponse(1, false), "", "");
			}
			
		});
	}
	
	private boolean compareResult(BufferedReader outputReader, StringBuilder outputBuilder, BufferedReader reader, StringBuilder builder) throws IOException {
		String line;
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
