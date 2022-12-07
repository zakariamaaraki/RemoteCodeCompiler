package com.cp.compiler.services.containers;

import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.exceptions.ProcessExecutionTimeoutException;
import com.cp.compiler.models.process.ProcessOutput;
import com.cp.compiler.utils.retries.RetryHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * The type Default container service.
 */
@Slf4j
@Primary
@Service
public class DefaultContainerService extends ContainerServiceDecorator {
    
    /**
     * The constant MAX_RETRIES.
     */
    public static final int MAX_RETRIES = 3;
    
    /**
     * The constant DURATION_BETWEEN_EACH_RETRY.
     */
    public static final int DURATION_BETWEEN_EACH_RETRY = 1000; // 1 sec
    
    /**
     * Instantiates a new Default container service.
     *
     * @param containerService the container service
     */
    protected DefaultContainerService(@Qualifier("docker") ContainerService containerService) {
        super(containerService);
    }
    
    @Override
    public String buildImage(String contextPath, String imageName, String dockerfileName) throws Exception {
        return RetryHelper.executeWithRetries(
                () -> getContainerService().buildImage(contextPath, imageName, dockerfileName),
                null,
                MAX_RETRIES,
                DURATION_BETWEEN_EACH_RETRY);
    }
    
    @Override
    public ProcessOutput runContainer(String imageName, long timeout, float maxCpus) {
        try {
            return RetryHelper.executeWithRetries(
                    () -> getContainerService().runContainer(imageName, timeout, maxCpus),
                    Set.of(ProcessExecutionTimeoutException.class.getName()),
                    MAX_RETRIES,
                    DURATION_BETWEEN_EACH_RETRY);
        } catch(Exception processExecutionException) {
            if (processExecutionException instanceof ProcessExecutionTimeoutException) {
                // TLE
                throw new ContainerOperationTimeoutException(processExecutionException.getMessage());
            }
            log.error("Error: {}", processExecutionException);
            throw new ContainerFailedDependencyException(processExecutionException.getMessage());
        }
    }
    
    @Override
    public ProcessOutput runContainer(String imageName,
                                      long timeout,
                                      String volumeMounting,
                                      String executionPath,
                                      String sourceCodeFileName) {
        try {
            return RetryHelper.executeWithRetries(
                    () -> getContainerService().runContainer(imageName, timeout, volumeMounting, executionPath, sourceCodeFileName),
                    Set.of(ProcessExecutionTimeoutException.class.getName()), // do not retry on timeout
                    MAX_RETRIES,
                    DURATION_BETWEEN_EACH_RETRY);
        } catch(Exception processExecutionException) {
            log.error("Error: {}", processExecutionException);
            if (processExecutionException instanceof ProcessExecutionTimeoutException) {
                throw new ContainerOperationTimeoutException(processExecutionException.getMessage());
            }
            throw new ContainerFailedDependencyException(processExecutionException.getMessage());
        }
    }
}
