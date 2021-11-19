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
	
	/**
	 *
	 * @return running containers
	 */
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
	
	
	/**
	 *
	 * @return docker images
	 */
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
	
	
	/**
	 *
	 * @return stats about running containers
	 */
	@RequestMapping(
			value = "stats",
			method = RequestMethod.GET
	)
	@ApiOperation(
			value = "Docker Stats Memory and CPU Usage",
			notes = "Display Stats about running containers (Memory and CPU usage)",
			response = Response.class
	)
	public ResponseEntity<String> getRunningContainersStats() {
		try {
			return ResponseEntity.ok().body(containerService.getContainersStats());
		} catch (IOException e) {
			return ResponseEntity.status(500).body("The server is currently unable to obtain this information");
		}
	}
	
	
	/**
	 *
	 * @return stats about all containers
	 */
	@RequestMapping(
			value = "stats/all",
			method = RequestMethod.GET
	)
	@ApiOperation(
			value = "Docker Stats Memory and CPU Usage for all containers",
			notes = "Display Stats about all containers (Memory and CPU usage)",
			response = Response.class
	)
	public ResponseEntity<String> getAllContainersStats() {
		try {
			return ResponseEntity.ok().body(containerService.getAllContainersStats());
		} catch (IOException e) {
			return ResponseEntity.status(500).body("The server is currently unable to obtain this information");
		}
	}
}
