package com.cp.compiler.utility;

import com.cp.compiler.model.Language;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Slf4j
public class EntryPointFile {
	
	private EntryPointFile() {
	}
	
	/**
	 *
	 * @param timeLimit the expected time limit that execution must not exceed
	 * @param memoryLimit the expected memory limit
	 * @param inputFile the input file that contains input data (can be null)
	 */
	@SneakyThrows
	public static void createPythonEntrypointFile(int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + "s " + Language.PYTHON.getCommand() + " main.py" + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + "s " + Language.PYTHON.getCommand() + " main.py" + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File(Language.PYTHON.getFolder() + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}
	
	/**
	 *
	 * @param fileName the source code file name (in java a class public must have the same name as the file name)
	 * @param timeLimit the expected time limit that execution must not exceed
	 * @param memoryLimit the expected memory limit
	 * @param inputFile the input file that contains input data (can be null)
	 */
	@SneakyThrows
	public static void createJavaEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0, fileName.length() - 5) + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0, fileName.length() - 5) + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				"mv main.java " + fileName + "\n" +
				Language.JAVA.getCommand() + " " + fileName + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File(Language.JAVA.getFolder() + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}
	
	/**
	 *
	 * @param timeLimit the expected time limit that execution must not exceed
	 * @param memoryLimit the expected memory limit
	 * @param inputFile the input file that contains input data (can be null)
	 */
	@SneakyThrows
	public static void createCEntrypointFile(int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " ./exec " + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				Language.C.getCommand() + " main.c" + " -o exec" + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File(Language.C.getFolder() + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}
	
	/**
	 *
	 * @param timeLimit the expected time limit that execution must not exceed
	 * @param memoryLimit the expected memory limit
	 * @param inputFile the input file that contains input data (can be null)
	 */
	@SneakyThrows
	public static void createCppEntrypointFile(int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " ./exec " + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				Language.CPP.getCommand() + " main.cpp" + " -o exec" + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File(Language.CPP.getFolder() + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}
	
}
