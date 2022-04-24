package com.cp.compiler.services;

import com.cp.compiler.exceptions.CompilerServerException;
import com.cp.compiler.exceptions.DockerBuildException;
import com.cp.compiler.models.Language;
import com.cp.compiler.models.Request;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Result;
import com.cp.compiler.utilities.EntryPoint;
import com.cp.compiler.utilities.FilesUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Compiler Service Class, this class provides compilation utilities for several programing languages
 *
 * @author Zakaria Maaraki
 */

@Slf4j
@Service
public class CompilerServiceImpl implements CompilerService {
    
    private final ContainerService containerService;
    
    @Getter
    @Value("${compiler.docker.image.delete:true}")
    private boolean deleteDockerImage;
    
    @Getter
    @Value("${compiler.execution-memory.max:10000}")
    private int maxExecutionMemory;
    
    @Getter
    @Value("${compiler.execution-memory.min:0}")
    private int minExecutionMemory;
    
    @Getter
    @Value("${compiler.execution-time.max:15}")
    private int maxExecutionTime;
    
    @Getter
    @Value("${compiler.execution-time.min:0}")
    private int minExecutionTime;
    
    public CompilerServiceImpl(ContainerService containerService) {
        this.containerService = containerService;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> compile(Request request) throws CompilerServerException, IOException {
        return compile(
                request.getExpectedOutput(),
                request.getSourceCode(),
                request.getInput(),
                request.getTimeLimit(),
                request.getMemoryLimit(),
                request.getLanguage());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> compile(MultipartFile outputFile,
                                          MultipartFile sourceCode,
                                          MultipartFile inputFile,
                                          int timeLimit,
                                          int memoryLimit,
                                          Language language) throws CompilerServerException {
        
        // Unique image name
        String imageName = UUID.randomUUID().toString();
    
        LocalDateTime date = LocalDateTime.now();
        
        if (memoryLimit < minExecutionMemory || memoryLimit > maxExecutionMemory) {
            log.info(imageName + " Error memoryLimit must be between {}Mb and {}Mb, provided : {}",
                     minExecutionMemory,
                     maxExecutionMemory,
                     memoryLimit);
            
            return ResponseEntity
                    .badRequest()
                    .body("Error memoryLimit must be between "
                            + minExecutionMemory + "Mb and " + maxExecutionMemory + "Mb, provided : " + memoryLimit);
        }
        
        
        if (timeLimit < minExecutionTime || timeLimit > maxExecutionTime) {
            log.info(imageName + " Error timeLimit must be between {} Sec and {} Sec, provided : {}",
                     minExecutionTime,
                     maxExecutionTime,
                     timeLimit);
            
            return ResponseEntity
                    .badRequest()
                    .body("Error timeLimit must be between "
                            + minExecutionTime + " Sec and " + maxExecutionTime + " Sec, provided : " + timeLimit);
        }
    
        builderDockerImage(outputFile, sourceCode, inputFile, timeLimit, memoryLimit, language, imageName);
    
        Result result = containerService.runCode(imageName, outputFile);
        
        if (deleteDockerImage) {
            try {
                containerService.deleteImage(imageName);
                log.info("Image " + imageName + " has been deleted");
            } catch (Exception e) {
                log.warn("Error, can't delete image {} : {}", imageName, e);
            }
        }
        
        String statusResponse = result.getVerdict();
        log.info(imageName + " Status response is " + statusResponse);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(result.getOutput(), result.getExpectedOutput(), statusResponse, date));
    }
    
    private void builderDockerImage(MultipartFile outputFile,
                                    MultipartFile sourceCode,
                                    MultipartFile inputFile,
                                    int timeLimit,
                                    int memoryLimit,
                                    Language language,
                                    String imageName) throws CompilerServerException {
        
        String folder = language.getFolder().concat("/").concat(imageName);
        String file = language.getFile();
        
        try {
            // Create directory before saving uploaded files
            Files.createDirectory(Path.of(folder));
        
            EntryPoint.createEntrypointFile(sourceCode, inputFile, timeLimit, memoryLimit, language, folder);
            log.info(imageName + " entrypoint.sh file has been created");
        
            FilesUtil.saveUploadedFiles(sourceCode, folder + "/" + file);
            FilesUtil.saveUploadedFiles(outputFile, folder + "/" + outputFile.getOriginalFilename());
            if (inputFile != null) {
                FilesUtil.saveUploadedFiles(inputFile, folder + "/" + inputFile.getOriginalFilename());
            }
            log.info(imageName + " Files have been uploaded");
        } catch (Exception e) {
            throw new CompilerServerException(imageName + " " + e.getMessage());
        }
        
        try {
            // Copy Dockerfile to the current execution folder
            try {
                FilesUtil.copyFile(language.getFolder().concat("/Dockerfile"), folder.concat("/Dockerfile"));
            } catch(Exception e) {
                throw new DockerBuildException(imageName + " Error while copying the DockerFile from "
                        + language.getFolder() + " to " + folder);
            }
        
            log.info(imageName + " Building the docker image");
            int status = containerService.buildImage(folder, imageName);
            if (status == 0) {
                log.info(imageName + " Docker image has been built");
            } else {
                throw new DockerBuildException(imageName + " Error while building docker image");
            }
        } finally {
            // Delete utility folder
            try {
                FileSystemUtils.deleteRecursively(Path.of(folder));
                log.info(imageName + " folder {} has been deleted", folder);
            } catch (IOException e) {
                log.warn("Error while trying to delete the folder {}, {}", folder, e);
            }
        }
    }
}
