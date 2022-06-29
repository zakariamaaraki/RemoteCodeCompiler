package com.cp.compiler.services;

import com.cp.compiler.models.ContainerOutput;
import com.cp.compiler.models.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The interface Container service.
 */
public interface ContainerService {
    
    /**
     * Build image int.
     *
     * @param folder    the folder
     * @param imageName the image name
     * @return the int the status code
     */
    int buildImage(String folder, String imageName);
    
    /**
     * Run container container output.
     *
     * @param imageName the image name
     * @param timeout   the timeout
     * @return the container output
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    ContainerOutput runContainer(String imageName, long timeout) throws IOException, InterruptedException;
    
    /**
     * Gets running containers.
     *
     * @return the running containers
     * @throws IOException the io exception
     */
    String getRunningContainers() throws IOException;
    
    /**
     * Gets images.
     *
     * @return the images
     * @throws IOException the io exception
     */
    String getImages() throws IOException;
    
    /**
     * Gets containers stats.
     *
     * @return the containers stats
     * @throws IOException the io exception
     */
    String getContainersStats() throws IOException;
    
    /**
     * Gets all containers stats.
     *
     * @return the all containers stats
     * @throws IOException the io exception
     */
    String getAllContainersStats() throws IOException;
    
    /**
     * Delete image string.
     *
     * @param imageName the image name
     * @return the string
     * @throws IOException the io exception
     */
    String deleteImage(String imageName) throws IOException;
    
}
