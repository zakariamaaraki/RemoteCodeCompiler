package com.cp.compiler.api.controllers;

import com.cp.compiler.contract.resources.AvailableResources;
import com.cp.compiler.services.platform.resources.Resources;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Resource controller.
 *
 * @author Zakaria Maaraki
 */
@RestController
@RequestMapping("/api")
public class ResourceController {
    
    private final Resources resources;
    
    /**
     * Instantiates a new Resource controller.
     *
     * @param resources the resources
     */
    public ResourceController(Resources resources)  {
        this.resources = resources;
    }
    
    /**
     * Gets available resources.
     *
     * @return the available resources
     */
    @ApiOperation(value = "Return available resources")
    @GetMapping("/availableResources")
    public AvailableResources getAvailableResources() {
        return resources.getAvailableResources();
    }
}
