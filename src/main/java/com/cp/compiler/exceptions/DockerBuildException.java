package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Docker Build Exception Class
 *
 * @author Zakaria Maaraki
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DockerBuildException extends RuntimeException {
	
	public DockerBuildException(String message) {
		super(message);
	}
	
}
