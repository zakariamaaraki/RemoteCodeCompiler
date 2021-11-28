package com.cp.compiler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Language.
 *
 * @author Zakaria Maaraki
 */
@Getter
@AllArgsConstructor
public enum Language {
	
	/**
	 * Python language.
	 */
	PYTHON("utility_py", "main.py", "python3"),
	/**
	 * C language.
	 */
	C("utility_c", "main.c", "gcc"),
	/**
	 * Cpp language.
	 */
	CPP("utility_cpp", "main.cpp", "g++"),
	/**
	 * Java language.
	 */
	JAVA("utility_java", "main.java", "javac");
	
	/**
	 * The Folder.
	 */
	String folder;
	/**
	 * The File.
	 */
	String file;
	/**
	 * The Command.
	 */
	String command;
}