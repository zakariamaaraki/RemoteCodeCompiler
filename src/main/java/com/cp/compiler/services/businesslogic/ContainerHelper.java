package com.cp.compiler.services.businesslogic;

import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.containers.ContainerInfo;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.services.containers.ContainerService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;

@Slf4j
public abstract class ContainerHelper {
    
    private ContainerHelper() {}
    
    public static void deleteContainer(String containerName,
                                       ContainerService containerService,
                                       ExecutorService threadPool) {
        threadPool.execute(() -> {
            containerService.deleteContainer(containerName);
        });
    }
    
    public static void deleteImage(String imageName, ContainerService containerService, ExecutorService threadPool) {
        threadPool.execute(() -> {
            containerService.deleteImage(imageName);
        });
    }
    
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
    
    public static int getExecutionDuration(LocalDateTime startTime, LocalDateTime endTime, int executionDuration) {
        if (startTime == null || endTime == null) {
            return executionDuration;
        }
        var zdEndTime = ZonedDateTime.of(endTime, ZoneId.systemDefault());
        var zdStartTime = ZonedDateTime.of(startTime, ZoneId.systemDefault());
        return (int)(zdEndTime.toInstant().toEpochMilli() - zdStartTime.toInstant().toEpochMilli());
    }
    
    public static void cleanStdErrOutput(ProcessOutput processOutput, Execution execution) {
        // Don't return the absolut path to the user
        processOutput.setStdErr(processOutput.getStdErr().replace(execution.getPath(), ""));
    }
}
