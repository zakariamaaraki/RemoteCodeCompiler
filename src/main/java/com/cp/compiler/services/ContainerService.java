package com.cp.compiler.services;

import com.cp.compiler.models.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The interface Container service.
 *
 * @author: Zakaria Maaraki
 */
public interface ContainerService {
	
	/**
	 * Build image int.
	 *
	 * @param folder    path to the folder where the Dockerfile exists
	 * @param imageName the name of the Docker image
	 * @return an Integer represent the status (0 means ok, other value means that something went wrong)
	 */
	int buildImage(String folder, String imageName);
	
	/**
	 * Run code result.
	 *
	 * @param imageName  the Docker image name
	 * @param outputFile the expected output result to compare the output result of the container with the expected output
	 * @return an Object Result, represents the result of the code execution
	 */
	Result runCode(String imageName, MultipartFile outputFile);
	
	/**
	 * Gets running containers.
	 *
	 * @return a string displaying information about running containers
	 * @throws IOException an exception
	 */
	String getRunningContainers() throws IOException;
	
	/**
	 * Gets images.
	 *
	 * @return a string displaying information about docker images
	 * @throws IOException an exception
	 */
	String getImages() throws IOException;
	
	/**
	 * Gets containers stats.
	 *
	 * @return a String displaying stats about running containers (Mem Usage, Mem Limit, CPU)
	 * @throws IOException an exception
	 */
	String getContainersStats() throws IOException;
	
	/**
	 * Gets all containers stats.
	 *
	 * @return a String displaying stats about all containers (Mem Usage, Mem Limit, CPU)
	 * @throws IOException an exception
	 */
	String getAllContainersStats() throws IOException;
	
	/**
	 * Delete image string.
	 *
	 * @param imageName docker image name
	 * @return a string displaying the removed docker image
	 * @throws IOException an exception
	 */
	String deleteImage(String imageName) throws IOException;
	
}
