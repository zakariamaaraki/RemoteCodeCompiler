package com.cp.compiler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Verdict.
 *
 * @author Zakaria Maaraki
 */
@AllArgsConstructor
public enum Verdict {
	
	/**
	 * Accepted verdict.
	 */
	ACCEPTED("Accepted"),
	/**
	 * The Wrong answer.
	 */
	WRONG_ANSWER("Wrong Answer"),
	/**
	 * The Compilation error.
	 */
	COMPILATION_ERROR("Compilation Error"),
	/**
	 * The Out of memory.
	 */
	OUT_OF_MEMORY("Out Of Memory"),
	/**
	 * The Time limit exceeded.
	 */
	TIME_LIMIT_EXCEEDED("Time Limit Exceeded"),
	/**
	 * The Runtime error.
	 */
	RUNTIME_ERROR("Runtime Error");
	
	/**
	 * The Value.
	 */
	@Getter
	String value;
}
