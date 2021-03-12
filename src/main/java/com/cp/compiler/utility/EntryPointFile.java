package com.cp.compiler.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EntryPointFile {
	
	// create Python entrypoint.sh file
	public static void createPythonEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " python3 main.py" + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " python3 main.py" + " < " + inputFile.getOriginalFilename() + "\n";
		String content = "#!/usr/bin/env bash\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File("utility_py/entrypoint.sh"));
			os.write(content.getBytes(), 0, content.length());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// create Java entrypoint.sh file
	public static void createJavaEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0,fileName.length() - 5) + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " java " + fileName.substring(0,fileName.length() - 5) + " < " + inputFile.getOriginalFilename() + "\n";
		String content = "#!/usr/bin/env bash\n" +
				"mv main.java " + fileName+ "\n" +
				"javac " + fileName + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File("utility/entrypoint.sh"));
			os.write(content.getBytes(), 0, content.length());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// create C entrypoint.sh file
	public static void createCEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " ./exec " + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
		String content = "#!/usr/bin/env bash\n" +
				"gcc main.c" + " -o exec" + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File("utility_c/entrypoint.sh"));
			os.write(content.getBytes(), 0, content.length());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// create CPP entrypoint.sh file
	public static void createCppEntrypointFile(String fileName, int timeLimit, int memoryLimit, MultipartFile inputFile) {
		String executionCommand = inputFile == null
				? "timeout --signal=SIGTERM " + timeLimit + " ./exec " + "\n"
				: "timeout --signal=SIGTERM " + timeLimit + " ./exec " + " < " + inputFile.getOriginalFilename() + "\n";
		String content = "#!/usr/bin/env bash\n" +
				"g++ main.cpp" + " -o exec" + "\n" +
				"ret=$?\n" +
				"if [ $ret -ne 0 ]\n" +
				"then\n" +
				"  exit 2\n" +
				"fi\n" +
				"ulimit -s " + memoryLimit + "\n" +
				executionCommand +
				"exit $?\n";
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File("utility_cpp/entrypoint.sh"));
			os.write(content.getBytes(), 0, content.length());
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
