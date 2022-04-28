package com.cp.compiler.services;

import com.cp.compiler.models.Result;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.utilities.CmdUtil;
import com.cp.compiler.utilities.StatusUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * This class provides Docker utilities that are used by the compiler
 *
 * @author Zakaria Maaraki
 */

@Slf4j
@Service
public class ContainerServiceImpl implements ContainerService {
    
    private static final long TIME_OUT = 20000; // in ms
    private static final int TIME_LIMIT_STATUS_CODE = 124;
    
    private final MeterRegistry meterRegistry;
    private Timer buildTimer;
    private Timer runTimer;
    
    public ContainerServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    @PostConstruct
    public void init() {
        buildTimer = meterRegistry.timer("container.build", "container", "docker");
        runTimer = meterRegistry.timer("container.run", "container", "docker");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int buildImage(String folder, String imageName) {
        return buildTimer.record(() -> {
            try {
                String[] dockerCommand = new String[]{"docker", "image", "build", folder, "-t", imageName};
                ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
                Process process = processbuilder.start();
                return process.waitFor();
            } catch (Exception e) {
                log.error("Error during the build process : ", e);
                return 1;
            }
        });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Result runCode(String imageName, MultipartFile outputFile, int timeLimit) {
        
        return runTimer.record(() -> {
            
            try {
                int status;
                
                BufferedReader expectedOutputReader =
                        new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
                String expectedOutput = CmdUtil.readOutput(expectedOutputReader);
                
                log.info(imageName + " Running the container");
                String[] dockerCommand = new String[]{"docker", "run", "--rm", imageName};
                ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
                
                long executionStartTime = System.currentTimeMillis();
                Process process = processbuilder.start();
                
                // Do not let the container exceed the timeout
                process.waitFor(TIME_OUT, TimeUnit.MILLISECONDS);
                long executionEndTime = System.currentTimeMillis();
                
                // Check if the container process is alive,
                // if it's so then destroy it and return a time limit exceeded status
                if (process.isAlive()) {
                    status = TIME_LIMIT_STATUS_CODE;
                    log.info(imageName + " The container exceed the 20 sec allowed for its execution");
                    process.destroy();
                    log.info(imageName + " The container has been destroyed");
                    
                    /* Can't get the output from the container (because it did not finish it's execution),
                       so we assume that the comparison between the output and the excepted output returns false */
                    Verdict verdict = StatusUtil.statusResponse(status, false);
                    
                    return new Result(
                            verdict, "", expectedOutput, timeLimit * 1000 + 1);
                } else {
                    status = process.exitValue();
                    log.info(imageName + " End of the execution of the container");
                    
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String containerOutput = CmdUtil.readOutput(reader);
                    
                    boolean result = compareResult(containerOutput, expectedOutput);
                    Verdict verdict = StatusUtil.statusResponse(status, result);
                    
                    return new Result(
                            verdict,
                            containerOutput,
                            expectedOutput,
                            executionEndTime - executionStartTime);
                }
            } catch (Exception e) {
                log.error("Error : ", e);
                return new Result(StatusUtil.statusResponse(1, false),
                                  "A server side error has occurred", "", 0);
            }
        });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRunningContainers() throws IOException {
        return CmdUtil.runCmd("docker", "ps");
    }
    
    @Override
    public String getContainersStats() throws IOException {
        return CmdUtil.runCmd("docker", "stats", "--no-stream");
    }
    
    @Override
    public String getAllContainersStats() throws IOException {
        return CmdUtil.runCmd("docker", "stats", "--no-stream", "--all");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getImages() throws IOException {
        return CmdUtil.runCmd("docker", "images");
    }
    
    @Override
    public String deleteImage(String imageName) throws IOException {
        return CmdUtil.runCmd("docker", "rmi", "-f", imageName);
    }
    
    // Remove \n and extra spaces
    private static boolean compareResult(String containerOutput, String expectedOutput) {
        return containerOutput
                .trim()
                .replaceAll("\\s+", " ")
                .replaceAll("/n","")
                .equals(expectedOutput.trim()
                                      .replaceAll("\\s+", " ")
                                      .replaceAll("/n", ""));
    }
}
