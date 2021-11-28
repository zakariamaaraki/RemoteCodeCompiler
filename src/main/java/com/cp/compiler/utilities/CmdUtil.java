package com.cp.compiler.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The type Cmd util.
 *
 * @author Zakaria Maaraki
 */
public class CmdUtil {
	
	private CmdUtil() {}
	
	/**
	 * Run cmd string.
	 *
	 * @param params the params
	 * @return the string
	 * @throws IOException the io exception
	 */
	public static String runCmd(String... params) throws IOException {
		String[] dockerCommand = params;
		ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
		Process process = processbuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		return readOutput(reader);
	}
	
	/**
	 * Read output string.
	 *
	 * @param reader the reader
	 * @return the string
	 * @throws IOException the io exception
	 */
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
