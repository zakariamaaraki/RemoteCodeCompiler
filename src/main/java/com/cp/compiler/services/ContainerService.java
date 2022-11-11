package com.cp.compiler.services;

import com.cp.compiler.models.ProcessOutput;

import java.io.IOException;

/**
 * The interface Container service.
 */
public interface ContainerService {
    
    /**
     * Build image int.
     *
     * @param contextPath    the context path
     * @param imageName      the image name
     * @param dockerfileName the dockerfile path
     * @return The build log
     */
    String buildImage(String contextPath, String imageName, String dockerfileName);
    
    /**
     * Run container container output.
     *
     * @param imageName the image name
     * @param timeout   the timeout
     * @param maxCpus   the max cpus
     * @return the container output
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    ProcessOutput runContainer(String imageName, long timeout, float maxCpus);
    
    /**
     * Run container process output.
     *
     * @param imageName      the image name
     * @param timeout        the timeout
     * @param volumeMounting the volume mounting
     * @return the process output
     */
    ProcessOutput runContainer(String imageName, long timeout, String volumeMounting);
    
    /**
     * Gets running containers.
     *
     * @return the running containers
     * @throws IOException the io exception
     */
    String getRunningContainers();
    
    /**
     * Gets images.
     *
     * @return the images
     * @throws IOException the io exception
     */
    String getImages();
    
    /**
     * Gets containers stats.
     *
     * @return the containers stats
     * @throws IOException the io exception
     */
    String getContainersStats();
    
    /**
     * Gets all containers stats.
     *
     * @return the all containers stats
     * @throws IOException the io exception
     */
    String getAllContainersStats();
    
    /**
     * Delete image string.
     *
     * @param imageName the image name
     * @return the string
     * @throws IOException the io exception
     */
    String deleteImage(String imageName);
    
    /**
     * Is up boolean.
     *
     * @return the boolean if the container Engine is up and running
     */
    boolean isUp();
    
    /**
     * Gets containerization name.
     *
     * @return the containerization name
     */
    String getContainerizationName();
}
