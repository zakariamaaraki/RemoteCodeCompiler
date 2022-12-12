package com.cp.compiler.services.containers;

import com.cp.compiler.models.containers.ContainerInfo;
import lombok.Getter;

/**
 * The type Container service decorator.
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
    public String deleteImage(String imageName) {
        return containerService.deleteImage(imageName);
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
        return containerService.inspect(containerName);
    }
    
    @Override
    public void deleteContainer(String containerName) {
        containerService.deleteContainer(containerName);
    }
}
