package com.cp.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
	
	PYTHON("utility_py", "main.py", "python3"),
	C("utility_c", "main.c", "gcc"),
	CPP("utility_cpp", "main.cpp", "g++"),
	JAVA("utility_java", "main.java", "javac");
	
	String folder;
	String file;
	String command;
}