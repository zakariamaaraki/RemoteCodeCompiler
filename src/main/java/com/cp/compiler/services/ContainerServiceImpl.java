package com.cp.compiler.services;

import com.cp.compiler.exceptions.ContainerExecutionException;
import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ProcessExecutionException;
import com.cp.compiler.models.ProcessOutput;
import com.cp.compiler.models.WellKnownMetrics;
import com.cp.compiler.utilities.CmdUtil;
import com.cp.compiler.utilities.StatusUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    
    private static final long COMMAND_TIMEOUT = 2000;
    
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
    public ProcessOutput runContainer(String imageName, long timeout) {
        return runTimer.record(() -> {
            try {
                var cpus = "--cpus=" + resources.getMaxCpus();
                String[] dockerCommand = new String[]{"docker", "run", cpus, "--rm", imageName};
                return CmdUtil.executeProcess(dockerCommand, timeout, StatusUtil.TIME_LIMIT_EXCEEDED_STATUS);
            } catch(ProcessExecutionException e) {
                var errorMessage = "Error during container execution: " + e.getMessage();
                throw new ContainerExecutionException(errorMessage);
            }
        });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRunningContainers() {
        String[] command = {"docker", "ps"};
        return executeContainerCommand(command);
    }
    
    @Override
    public String getContainersStats() {
        String[] command = {"docker", "stats", "--no-stream"};
        return executeContainerCommand(command);
    }
    
    @Override
    public String getAllContainersStats() {
        String[] command = {"docker", "stats", "--no-stream", "--all"};
        return executeContainerCommand(command);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getImages() {
        String[] command = {"docker", "images"};
        return executeContainerCommand(command);
    }
    
    @Override
    public String deleteImage(String imageName) {
        String[] command = {"docker", "rmi", "-f", imageName};
        return executeContainerCommand(command);
    }
    
    private String executeContainerCommand(String[] command) {
        try {
            ProcessOutput processOutput = CmdUtil.executeProcess(command, COMMAND_TIMEOUT, -1);
            return processOutput.getStdOut();
        } catch (ProcessExecutionException e) {
            throw new ContainerFailedDependencyException(e.getMessage());
        }
    }
}
