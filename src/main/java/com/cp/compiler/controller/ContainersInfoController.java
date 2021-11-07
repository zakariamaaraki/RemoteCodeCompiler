package com.cp.compiler.controller;

import com.cp.compiler.model.Response;
import com.cp.compiler.service.ContainService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/docker")
public class ContainersInfoController {
	
	private ContainService containService;
	
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
			return ResponseEntity.ok().body(containService.getRunningContainers());
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
			return ResponseEntity.ok().body(containService.getImages());
		} catch (IOException e) {
			return ResponseEntity.status(500).body("The server is currently unable to obtain this information");
		}
	}
}
