package com.cp.compiler.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdUtil {
	
	private CmdUtil() {}
	
	public static String runCmd(String... params) throws IOException {
		String[] dockerCommand = params;
		ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
		Process process = processbuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		return readOutput(reader);
	}
	
	public static String readOutput(BufferedReader reader) throws IOException {
		String line;
		StringBuilder builder = new StringBuilder();
		
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		
		return builder.toString();
	}
}
