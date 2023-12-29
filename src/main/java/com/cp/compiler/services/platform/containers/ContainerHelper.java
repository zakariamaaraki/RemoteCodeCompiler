package com.cp.compiler.services.platform.containers;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.containers.ContainerInfo;
import com.cp.compiler.models.processes.ProcessOutput;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;

/**
 * The type Container helper.
 *
 * @author Zakaria Maaraki
 */
@Slf4j
public abstract class ContainerHelper {
    
    private ContainerHelper() {}
    
    /**
     * Delete container.
     *
     * @param containerName    the container name
     * @param containerService the container service
     * @param threadPool       the thread pool
     */
    public static void deleteContainer(String containerName,
                                       ContainerService containerService,
                                       ExecutorService threadPool) {
        threadPool.execute(() -> {
            containerService.deleteContainer(containerName);
        });
    }
    
    /**
     * Delete image.
     *
     * @param imageName        the image name
     * @param containerService the container service
     * @param threadPool       the thread pool
     */
    public static void deleteImage(String imageName, ContainerService containerService, ExecutorService threadPool) {
        threadPool.execute(() -> {
            containerService.deleteImage(imageName);
        });
    }
    
    /**
     * Log container info.
     *
     * @param containerName the container name
     * @param containerInfo the container info
     */
    public static void logContainerInfo(String containerName, ContainerInfo containerInfo) {
        if (containerInfo != null) {
            log.info("Container {} was created at {}, started at {}, ended at {}, has a status = {} and an error = {}",
                    containerName,
                    containerInfo.getCreationTime(),
                    containerInfo.getStartTime(),
                    containerInfo.getEndTime(),
                    containerInfo.getStatus(),
                    containerInfo.getError());
        }
    }
    
    /**
     * Gets execution duration.
     *
     * @param startTime         the start time
     * @param endTime           the end time
     * @param executionDuration the execution duration
     * @return the execution duration
     */
    public static int getExecutionDuration(LocalDateTime startTime, LocalDateTime endTime, int executionDuration) {
        if (startTime == null || endTime == null) {
            return executionDuration;
        }
        var zdEndTime = ZonedDateTime.of(endTime, ZoneId.systemDefault());
        var zdStartTime = ZonedDateTime.of(startTime, ZoneId.systemDefault());
        return (int)(zdEndTime.toInstant().toEpochMilli() - zdStartTime.toInstant().toEpochMilli());
    }
    
    /**
     * Clean std err output.
     *
     * @param processOutput the process output
     * @param execution     the execution
     */
    public static void cleanStdErrOutput(ProcessOutput processOutput, Execution execution) {
        if (processOutput == null || processOutput.getStdErr() == null) {
            return;
        }
        // Don't return the absolut path to the user
        processOutput.setStdErr(processOutput.getStdErr().replace(execution.getPath(), ""));
    }
}
