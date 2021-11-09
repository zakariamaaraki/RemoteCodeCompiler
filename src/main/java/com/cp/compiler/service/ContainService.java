package com.cp.compiler.service;

import com.cp.compiler.model.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ContainService {
	
	/**
	 *
	 * @param folder path to the folder where the Dockerfile exists
	 * @param imageName the name of the Docker image
	 * @return an Integer represent the status (0 means ok, other value means that something went wrong)
	 */
	int buildImage(String folder, String imageName);
	
	/**
	 *
	 * @param imageName the Docker image name
	 * @param outputFile the expected output result to compare the output result of the container with the expected output
	 * @return an Object Result, represents the result of the code execution
	 */
	Result runCode(String imageName, MultipartFile outputFile);
	
	/**
	 *
	 * @return a string displaying information about running containers
	 * @throws IOException
	 */
	String getRunningContainers() throws IOException;
	
	/**
	 *
	 * @return a string displaying information about docker images
	 * @throws IOException
	 */
	String getImages() throws IOException;
	
	/**
	 *
	 * @param imageName
	 * @return a string displaying the removed docker image
	 * @throws IOException
	 */
	String deleteImage(String imageName) throws IOException;
	
}
