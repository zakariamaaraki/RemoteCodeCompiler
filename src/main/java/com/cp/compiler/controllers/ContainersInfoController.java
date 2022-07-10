package com.cp.compiler.controllers;

import com.cp.compiler.models.Response;
import com.cp.compiler.services.ContainerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Container Info Rest Controller
 *
 * @author Zakaria Maaraki
 */
@RestController
@RequestMapping("/container")
public class ContainersInfoController {
    
    private ContainerService containerService;
    
    /**
     * Instantiates a new Containers info controller.
     *
     * @param containerService the container service
     */
    public ContainersInfoController(ContainerService containerService) {
        this.containerService = containerService;
    }
    
    /**
     * Gets running containers.
     *
     * @return running containers
     */
    @GetMapping("/containers")
    @ApiOperation(
            value = "Containers Info",
            notes = "Display list of running containers",
            response = Response.class
    )
    public ResponseEntity<String> getRunningContainers() {
        return ResponseEntity.ok().body(containerService.getRunningContainers());
    }
    
    /**
     * Gets images.
     *
     * @return container images
     */
    @GetMapping("/images")
    @ApiOperation(
            value = "Images Info",
            notes = "Display list of images",
            response = Response.class
    )
    public ResponseEntity<String> getImages() {
        return ResponseEntity.ok().body(containerService.getImages());
    }
    
    /**
     * Gets running containers stats.
     *
     * @return stats about running containers
     */
    @GetMapping("/stats")
    @ApiOperation(
            value = "Container Stats Memory and CPU Usage",
            notes = "Display Stats about running containers (Memory and CPU usage)",
            response = Response.class
    )
    public ResponseEntity<String> getRunningContainersStats() {
        return ResponseEntity.ok().body(containerService.getContainersStats());
    }
    
    /**
     * Gets all containers stats.
     *
     * @return stats about all containers
     */
    @GetMapping("stats/all")
    @ApiOperation(
            value = "Stats of Memory and CPU Usage for all containers",
            notes = "Display Stats about all containers (Memory and CPU usage)",
            response = Response.class
    )
    public ResponseEntity<String> getAllContainersStats() {
        return ResponseEntity.ok().body(containerService.getAllContainersStats());
    }
}
