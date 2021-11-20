package com.cp.compiler.service;

import com.cp.compiler.exceptions.CompilerServerException;
import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.model.Languages;
import com.cp.compiler.model.Response;
import com.cp.compiler.model.Result;
import com.cp.compiler.utility.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cp.compiler.utility.EntryPointFile.*;

/**
 * Compiler Service Class, this class provides compilation utilities for several programing languages
 * @author Zakaria Maaraki
 */

@Slf4j
@Service
public class CompilerServiceImpl implements CompilerService {
	
	@Autowired
	private ContainerService containerService;
	
	@Value("${compiler.docker.image.delete:true}")
	private boolean deleteDockerImage;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Object> compile(MultipartFile outputFile, MultipartFile sourceCode, MultipartFile inputFile,
	                                      int timeLimit, int memoryLimit, Languages languages) throws CompilerServerException {
		
		// Unique image name
		String imageName = UUID.randomUUID().toString();
		
		String folder = "utility";
		String file = "main";
		
		if (languages == Languages.C) {
			folder += "_c";
			file += ".c";
		} else if (languages == Languages.JAVA) {
			file += ".java";
		} else if (languages == Languages.CPP) {
			folder += "_cpp";
			file += ".cpp";
		} else {
			folder += "_py";
			file += ".py";
		}
		
		if (memoryLimit < 0 || memoryLimit > 1000)
			return ResponseEntity
					.badRequest()
					.body(imageName + " Error memoryLimit must be between 0Mb and 1000Mb");
		
		if (timeLimit < 0 || timeLimit > 15)
			return ResponseEntity
					.badRequest()
					.body(imageName + " Error timeLimit must be between 0 Sec and 15 Sec");
		
		LocalDateTime date = LocalDateTime.now();
		
		// Build one docker image at time
		synchronized (this) {
			
			createEntrypointFile(sourceCode, inputFile, timeLimit, memoryLimit, languages);
			
			log.info(imageName + " entrypoint.sh file has been created");
			
			try {
				FilesUtil.saveUploadedFiles(sourceCode, folder + "/" + file);
				FilesUtil.saveUploadedFiles(outputFile, folder + "/" + outputFile.getOriginalFilename());
				if (inputFile != null)
					FilesUtil.saveUploadedFiles(inputFile, folder + "/" + inputFile.getOriginalFilename());
			} catch (IOException e) {
				throw new CompilerServerException(e.getMessage());
			}
			
			log.info(imageName + " Files have been uploaded");
			
			try {
				log.info(imageName + " Building the docker image");
				
				AtomicInteger status = new AtomicInteger(containerService.buildImage(folder, imageName));
				
				if (status.get() == 0)
					log.info( imageName + " Docker image has been built");
				else {
					throw new DockerBuildException(imageName + " Error while building docker image");
				}
			} finally {
				// delete files
				FilesUtil.deleteFile(folder, file);
				FilesUtil.deleteFile(folder, outputFile.getOriginalFilename());
				if (inputFile != null)
					FilesUtil.deleteFile(folder, inputFile.getOriginalFilename());
			}
		}
		
		Result result = containerService.runCode(imageName, outputFile);
		
		if (deleteDockerImage) {
			try {
				containerService.deleteImage(imageName);
				log.info("Image " + imageName + " has been deleted");
			} catch (IOException e) {
				log.warn("Error, can't delete image " + imageName + " : ", e);
			}
		}
		
		String statusResponse = result.getVerdict();
		log.info(imageName + " Status response is " + statusResponse);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new Response(result.getOutput(), result.getExpectedOutput(), statusResponse, date));
	}
	
	private void createEntrypointFile(MultipartFile sourceCode, MultipartFile inputFile, int timeLimit, int memoryLimit, Languages languages) {
		if (languages == Languages.JAVA) {
			// The name of the class should be equals to the name of the file
			createJavaEntrypointFile(sourceCode.getOriginalFilename(), timeLimit, memoryLimit, inputFile);
		} else if (languages == Languages.C) {
			createCEntrypointFile(timeLimit, memoryLimit, inputFile);
		} else if (languages == Languages.CPP) {
			createCppEntrypointFile(timeLimit, memoryLimit, inputFile);
		} else {
			createPythonEntrypointFile(timeLimit, memoryLimit, inputFile);
		}
	}
}
