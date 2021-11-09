package com.cp.compiler.controller;

import com.cp.compiler.model.Response;
import com.cp.compiler.service.ContainerService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Docker Info Rest Controller
 * @author Zakaria Maaraki
 */

@RestController
@AllArgsConstructor
@RequestMapping("/docker")
public class ContainersInfoController {
	
	private ContainerService containerService;
	
	// List of Running containers
	@RequestMapping(
			value = "containers",
			method = RequestMethod.GET
	)
	@ApiOperation(
			value = "Containers Info",
			notes = "Display list of running containers",
			response = Response.class
	)
	public ResponseEntity<String> getRunningContainers() {
		try {
			return ResponseEntity.ok().body(containerService.getRunningContainers());
		} catch (IOException e) {
			return ResponseEntity.status(500).body("The server is currently unable to obtain this information");
		}
	}
	
	// List of Images
	@RequestMapping(
			value = "images",
			method = RequestMethod.GET
	)
	@ApiOperation(
			value = "Images Info",
			notes = "Display list of images",
			response = Response.class
	)
	public ResponseEntity<String> getImages() {
		try {
			return ResponseEntity.ok().body(containerService.getImages());
		} catch (IOException e) {
			return ResponseEntity.status(500).body("The server is currently unable to obtain this information");
		}
	}
}
