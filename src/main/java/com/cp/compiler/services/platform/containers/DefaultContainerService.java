package com.cp.compiler.services.platform.containers;

import com.cp.compiler.exceptions.ContainerFailedDependencyException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.exceptions.ProcessExecutionTimeoutException;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.utils.retries.RetryHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * The type Default container service.
 *
 * @author Zakaria Maaraki
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
    
    @SneakyThrows
    @Override
    protected String buildContainerImageInternal(String contextPath, String imageName, String dockerfileName) {
        return RetryHelper.executeWithRetries(
                () -> getContainerService().buildImage(contextPath, imageName, dockerfileName),
                null,
                MAX_RETRIES,
                DURATION_BETWEEN_EACH_RETRY);
    }
    
    @SneakyThrows
    @Override
    protected ProcessOutput runContainerInternal(String imageName,
                                                 String containerName,
                                                 long timeout,
                                                 float maxCpus,
                                                 Map<String, String> envVariables) {
        return RetryHelper.executeWithRetries(
                () -> getContainerService().runContainer(imageName, containerName, timeout, maxCpus, envVariables),
                Set.of(ProcessExecutionTimeoutException.class.getName()),
                MAX_RETRIES,
                DURATION_BETWEEN_EACH_RETRY);
    }
    
    @Override
    public ProcessOutput runContainer(String imageName,
                                      String containerName,
                                      long timeout,
                                      String volumeMounting,
                                      String executionPath,
                                      String sourceCodeFileName) {
        try {
            return RetryHelper.executeWithRetries(
                    () -> getContainerService().runContainer(
                            imageName,
                            containerName,
                            timeout,
                            volumeMounting,
                            executionPath,
                            sourceCodeFileName),
                    Set.of(ProcessExecutionTimeoutException.class.getName()), // do not retry on timeout
                    MAX_RETRIES,
                    DURATION_BETWEEN_EACH_RETRY);
        } catch(Exception processExecutionException) {
            log.error("Error: ", processExecutionException);
            if (processExecutionException instanceof ProcessExecutionTimeoutException) {
                throw new ContainerOperationTimeoutException(processExecutionException.getMessage());
            }
            throw new ContainerFailedDependencyException(processExecutionException.getMessage());
        }
    }
}
