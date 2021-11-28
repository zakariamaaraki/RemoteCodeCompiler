package com.cp.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Compiler server exception.
 *
 * @author: Zakaria Maaraki
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CompilerServerException extends Exception {
	
	/**
	 * Instantiates a new Compiler server exception.
	 *
	 * @param message the message
	 */
	public CompilerServerException(String message) {
		super(message);
	}
}
