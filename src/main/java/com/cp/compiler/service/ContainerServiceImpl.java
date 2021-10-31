package com.cp.compiler.service;

import com.cp.compiler.model.Result;
import com.cp.compiler.utility.StatusUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ContainerServiceImpl implements ContainService {
	
	@Autowired
	private MeterRegistry meterRegistry;
	
	private Timer buildTimer;
	private Timer runTimer;
	
	// in millis
	private static final long TIME_OUT = 20000;
	
	private static final int TIME_LIMIT_STATUS_CODE = 127;
	
	@PostConstruct
	public void init() {
		buildTimer = meterRegistry.timer("compiler", "container", "build");
		runTimer = meterRegistry.timer("compiler", "container", "run");
	}
	
	@Override
	public int buildImage(String folder, String imageName) {
		return buildTimer.record(() -> {
			try {
				String[] dockerCommand = new String[] {"docker", "image", "build", folder, "-t", imageName};
				ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
				Process process = processbuilder.start();
				return process.waitFor();
			} catch (Exception e) {
				log.error("Error : ", e);
				// Error during the build
				return 1;
			}
		});
	}
	
	@Override
	public Result runCode(String folder, String imageName, MultipartFile outputFile) {
		
		return runTimer.record(() -> {
			
			try {
				int status = 0;
				
				BufferedReader expectedOutputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
				String expectedOutput = readOutput(expectedOutputReader);
				
				log.info("Running the container");
				String[] dockerCommand = new String[] {"docker", "run", "--rm", imageName};
				ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
				Process process = processbuilder.start();
				
				// Do not let the container exceed the timeout
				process.waitFor(TIME_OUT, TimeUnit.MILLISECONDS);
				
				// Check if the container process is alive, if it's so then destroy it and return a time limit exceeded status
				if(process.isAlive()) {
					status = TIME_LIMIT_STATUS_CODE;
					log.info("The container exceed the 20 sec allowed for its execution");
					process.destroy();
					log.info("The container has been destroyed");
					
					/**
					 * Can't get the output from the container (because it did not finish it's execution),
					 * so we assume that the comparison between the output and the excepted output return false
					 */
					String statusResponse = StatusUtil.statusResponse(status, false);
					return new Result(statusResponse, "No available output", expectedOutput);
				} else {
					status = process.exitValue();
					log.info("End of the execution of the container");
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String containerOutput = readOutput(reader);
					
					boolean result = compareResult(containerOutput, expectedOutput);
					String statusResponse = StatusUtil.statusResponse(status, result);
					return new Result(statusResponse, containerOutput, expectedOutput);
				}
			} catch (Exception e) {
				log.error("Error : ", e);
				return new Result(StatusUtil.statusResponse(1, false), "A server side error has occurred", "");
			}

			
		});
	}
	
	private String readOutput(BufferedReader reader) throws IOException {
		String line;
		String outputLine = null;
		StringBuilder builder = new StringBuilder();
		
		while ( (line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		
		return builder.toString();
	}
	
	private boolean compareResult(String containerOutput, String expectedOutput) {
		return containerOutput.trim().equals(expectedOutput.trim());
	}
}
