package com.cp.compiler.services.platform.containers;

import com.cp.compiler.exceptions.ContainerBuildException;
import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.exceptions.ProcessExecutionTimeoutException;
import com.cp.compiler.models.containers.ContainerInfo;
import com.cp.compiler.models.processes.ProcessOutput;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
/**
 * The type Container service decorator.
 *
 * @author Zakaria Maaraki
 */
public abstract class ContainerServiceDecorator implements ContainerService {
    
    @Getter
    private ContainerService containerService;
    
    /**
     * Instantiates a new Container service decorator.
     *
     * @param containerService the container service
     */
    protected ContainerServiceDecorator(ContainerService containerService) {
        this.containerService = containerService;
    }
    
    protected abstract String buildContainerImageInternal(String contextPath, String imageName, String dockerfileName);
    
    protected abstract ProcessOutput runContainerInternal(String imageName,
                                                          String containerName,
                                                          long timeout,
                                                          float maxCpus,
                                                          Map<String, String> envVariables);
    
    @Override
    public String getRunningContainers() {
        return containerService.getRunningContainers();
    }
    
    @Override
    public String getImages() {
        return containerService.getImages();
    }
    
    @Override
    public String getContainersStats() {
        return containerService.getContainersStats();
    }
    
    @Override
    public String getAllContainersStats() {
        return containerService.getAllContainersStats();
    }
    
    @Override
    public String buildImage(String contextPath, String imageName, String dockerfileName) {
        log.info("Start building the docker image: {}", imageName);
        String buildLogs = "";
        try {
            buildLogs = buildContainerImageInternal(contextPath, imageName, dockerfileName);
            log.debug("Build logs: {}", buildLogs);
        } catch(Exception exception) {
            log.error("Error while building container image: ", exception);
            throw new ContainerBuildException("Error while building compilation image: " + exception.getMessage());
        }
        return buildLogs;
    }
    
    @Override
    public ProcessOutput runContainer(String imageName,
                                      String containerName,
                                      long timeout,
                                      float maxCpus,
                                      Map<String, String> envVariables) {
        try {
            return runContainerInternal(imageName, containerName, timeout, maxCpus, envVariables);
        } catch(Exception processExecutionException) {
            if (processExecutionException instanceof ProcessExecutionTimeoutException) {
                // TLE
                throw new ContainerOperationTimeoutException(processExecutionException.getMessage());
            }
            log.error("Error while running the container: ", processExecutionException);
            throw new ContainerFailedDependencyException(processExecutionException.getMessage());
        }
    }
    
    @Override
    public void deleteImage(String imageName) {
        try {
            containerService.deleteImage(imageName);
            log.info("Image {} has been deleted", imageName);
        } catch (Exception e) {
            if (e instanceof ContainerOperationTimeoutException) {
                log.warn("Timeout, didn't get the response at time from container engine if the image {} was deleted",
                        imageName);
            } else {
                log.warn("Error, can't delete image {} : ", imageName, e);
            }
        }
    }
    
    @Override
    public boolean isUp() {
        return containerService.isUp();
    }
    
    @Override
    public String getContainerizationName() {
        return containerService.getContainerizationName();
    }
    
    @Override
    public ContainerInfo inspect(String containerName) {
        ContainerInfo containerInfo = null;
        try {
            containerInfo = containerService.inspect(containerName);
        } catch (Exception e) {
            log.warn("Unexpected error occurred during container inspection: ", e);
        }
        return containerInfo;
    }
    
    @Override
    public void deleteContainer(String containerName) {
        try {
            containerService.deleteContainer(containerName);
            log.info("Container {} has been deleted", containerName);
        } catch(Exception e) {
            log.warn("Unexpected error occurred while deleting the container {}: ", containerName, e);
        }
    }
}
