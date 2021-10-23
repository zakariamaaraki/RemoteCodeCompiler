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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ContainerServiceImpl implements ContainService {
	
	@Autowired
	private MeterRegistry meterRegistry;
	
	private Timer buildTimer;
	private Timer runTimer;
	
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
		
		AtomicInteger status = new AtomicInteger();
		
		return runTimer.record(() -> {
			log.info("Running the container");
			String[] dockerCommand = new String[] {"docker", "run", "--rm", imageName};
			ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
			Process process;
			try {
				process = processbuilder.start();
				status.set(process.waitFor());
				log.info("End of the execution of the container");
				
				BufferedReader outputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
				StringBuilder outputBuilder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder builder = new StringBuilder();
				
				boolean result = compareResult(outputReader, outputBuilder, reader, builder);
				String statusResponse = StatusUtil.statusResponse(status.get(), result);
				return new Result(statusResponse, builder.toString(), outputBuilder.toString());
			} catch (Exception e) {
				log.error("Error : ", e);
				return new Result(StatusUtil.statusResponse(1, false), "", "");
			}
			
		});
	}
	
	private boolean compareResult(BufferedReader outputReader, StringBuilder outputBuilder, BufferedReader reader, StringBuilder builder) throws IOException {
		
		String line;
		String outputLine = null;
		boolean ans = true;
		
		while ( (line = reader.readLine()) != null && (outputLine = outputReader.readLine()) != null) {
			if(!line.equals(outputLine))
				ans = false;
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
			
			outputBuilder.append(outputLine);
			outputBuilder.append(System.getProperty("line.separator"));
		}
		
		if(line != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		
		if(outputLine != null) {
			outputBuilder.append(outputLine);
			outputBuilder.append(System.getProperty("line.separator"));
		}
		
		while ( (line = reader.readLine()) != null) {
			ans = false;
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		
		while ( (outputLine = outputReader.readLine()) != null) {
			ans = false;
			outputBuilder.append(outputLine);
			outputBuilder.append(System.getProperty("line.separator"));
		}
		return ans;
	}
}
