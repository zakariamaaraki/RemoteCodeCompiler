package com.cp.compiler.services.platform.containers;

import com.cp.compiler.models.containers.ContainerInfo;
import com.cp.compiler.models.processes.ProcessOutput;

import java.io.IOException;
import java.util.Map;

/**
 * The interface Container service.
 *
 * @author Zakaria Maaraki
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
     * @param imageName     the image name
     * @param containerName the container name
     * @param timeout       the timeout
     * @param maxCpus       the max cpus
     * @param envVariables  the env variables
     * @return the container output
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    ProcessOutput runContainer(String imageName,
                               String containerName,
                               long timeout,
                               float maxCpus,
                               Map<String, String> envVariables);
    
    /**
     * Inspect a container.
     *
     * @param containerName the container name
     * @return the container info
     */
    ContainerInfo inspect(String containerName);
    
    /**
     * Delete a container
     *
     * @param containerName the container name
     */
    void deleteContainer(String containerName);
    
    /**
     * Run container process output.
     *
     * @param imageName          the image name
     * @param containerName      the container name
     * @param timeout            the timeout
     * @param volumeMounting     the volume mounting
     * @param executionPath      the execution path
     * @param sourceCodeFileName the source code file name
     * @return the process output
     */
    ProcessOutput runContainer(String imageName,
                               String containerName,
                               long timeout,
                               String volumeMounting,
                               String executionPath,
                               String sourceCodeFileName);
    
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
     * @throws IOException the io exception
     */
    void deleteImage(String imageName);
    
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
