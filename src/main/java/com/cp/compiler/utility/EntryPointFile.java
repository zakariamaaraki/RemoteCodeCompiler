package com.cp.compiler.utility;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Slf4j
public class EntryPointFile {
	
	private EntryPointFile() {}
	
	private static final String PYTHON_COMMAND = "python3";
	private static final String CPP_COMMAND = "g++";
	private static final String C_COMMAND = "gcc";
	private static final String JAVA_COMMAND = "javac";
	
	private static final String PYTHON_DIRECTORY = "utility_py";
	private static final String JAVA_DIRECTORY = "utility";
	private static final String C_DIRECTORY = "utility_c";
	private static final String CPP_DIRECTORY = "utility_cpp";
	
	// create Python entrypoint.sh file
	@SneakyThrows
	public static void createPythonEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + "s " + PYTHON_COMMAND + " main.py" + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + "s " + PYTHON_COMMAND + " main.py" + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File(PYTHON_DIRECTORY + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}
	
	// create Java entrypoint.sh file
	@SneakyThrows
	public static void createJavaEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0,fileName.length() - 5) + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0,fileName.length() - 5) + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				"mv main.java " + fileName+ "\n" +
				JAVA_COMMAND + " " + fileName + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File(JAVA_DIRECTORY + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}
	
	// create C entrypoint.sh file
	@SneakyThrows
	public static void createCEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " ./exec " + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				C_COMMAND + " main.c" + " -o exec" + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File( C_DIRECTORY + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}
	
	// create CPP entrypoint.sh file
	@SneakyThrows
	public static void createCppEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " ./exec " + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
		
		String content = "#!/usr/bin/env bash\n" +
				CPP_COMMAND + " main.cpp" + " -o exec" + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		
		OutputStream os = null;
		os = new FileOutputStream(new File(CPP_DIRECTORY + "/entrypoint.sh"));
		os.write(content.getBytes(), 0, content.length());
		os.close();
	}

}
