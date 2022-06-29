package com.cp.compiler.services;

import com.cp.compiler.exceptions.ContainerExecutionException;
import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.models.ContainerOutput;
import com.cp.compiler.models.Result;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.models.WellKnownMetrics;
import com.cp.compiler.utilities.CmdUtil;
import com.cp.compiler.utilities.StatusUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    
    private final MeterRegistry meterRegistry;
    private Timer buildTimer;
    private Timer runTimer;
    
    private final Resources resources;
    
    /**
     * Instantiates a new Container service.
     *
     * @param meterRegistry the meter registry
     * @param resources     the resources
     */
    public ContainerServiceImpl(MeterRegistry meterRegistry, Resources resources) {
        this.meterRegistry = meterRegistry;
        this.resources = resources;
    }
    
    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        buildTimer = meterRegistry.timer(WellKnownMetrics.CONTAINER_BUILD_TIMER, "container", "docker");
        runTimer = meterRegistry.timer(WellKnownMetrics.CONTAINER_RUN_TIMER, "container", "docker");
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
     * Run an instance of an image
     * @param imageName the image name
     * @param timeout   the timeout after which the container will be destroyed
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public ContainerOutput runContainer(String imageName, long timeout) {
        return runTimer.record(() -> {
            try {
                var cpus = "--cpus=" + resources.getMaxCpus();
                String[] dockerCommand = new String[]{"docker", "run", cpus, "--rm", imageName};
                ProcessBuilder processbuilder = new ProcessBuilder(dockerCommand);
    
                Process process = processbuilder.start();
                long executionStartTime = System.currentTimeMillis();
    
                // Do not let the container exceed the timeout
                process.waitFor(timeout, TimeUnit.MILLISECONDS);
                long executionEndTime = System.currentTimeMillis();
    
                int status = 0;
                String stdOut = "";
                String stdErr = "";
    
                // Check if the container process is alive,
                // if it's so then destroy it and return a time limit exceeded status
                if (process.isAlive()) {
                    status = StatusUtil.TIME_LIMIT_EXCEEDED_STATUS;
                    log.info("The container exceed the " + timeout + " Millis allowed for its execution");
                    process.destroy();
                    log.info("The container has been destroyed");
                } else {
                    status = process.exitValue();
        
                    BufferedReader containerOutputReader =
                            new BufferedReader(new InputStreamReader(process.getInputStream()));
                    stdOut = CmdUtil.readOutput(containerOutputReader);
        
                    BufferedReader containerErrorReader =
                            new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    stdErr = CmdUtil.buildErrorOutput(CmdUtil.readOutput(containerErrorReader));
                }
    
                return ContainerOutput
                        .builder()
                        .stdOut(stdOut)
                        .stdErr(stdErr)
                        .status(status)
                        .executionDuration(executionEndTime - executionStartTime)
                        .build();
                
            } catch(Exception e) {
                var errorMessage = "Error during container execution: " + e;
                throw new ContainerExecutionException(errorMessage);
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
}
