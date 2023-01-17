package com.cp.compiler.services.strategies;

import com.cp.compiler.exceptions.CompilationTimeoutException;
import com.cp.compiler.exceptions.ResourceLimitReachedException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.CompilationResponse;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.models.containers.ContainerInfo;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.services.businesslogic.ContainerHelper;
import com.cp.compiler.services.containers.ContainerService;
import com.cp.compiler.services.resources.Resources;
import com.cp.compiler.utils.StatusUtils;
import com.cp.compiler.wellknownconstants.WellKnownMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component("compiled")
public class CompiledLanguagesExecutionStrategy extends ExecutionStrategy {
    
    // Note: this value should not be updated, once update don't forget to update build.sh script used to build these images.
    private static final String IMAGE_PREFIX_NAME = "compiler.";
    
    // Note: changing this value is critical, it has a lot of impact on the compilation step
    private static final long COMPILATION_TIME_OUT = 60000; // in ms
    
    // Note: this value should not be updated, once update don't forget to update it also in all compilation Dockerfiles.
    private static final String EXECUTION_PATH_INSIDE_CONTAINER = "/app";
    
    @Value("${compiler.compilation-container.volume:}")
    private String compilationContainerVolume;
    
    private final MeterRegistry meterRegistry;
    
    private Timer compilationTimer;
    
    private final ContainerService containerService;
    
    /**
     * The compilation container name prefix
     */
    private static final String COMPILATION_CONTAINER_NAME_PREFIX = "compilation-";
    
    public CompiledLanguagesExecutionStrategy(ContainerService containerService,
                                              MeterRegistry meterRegistry,
                                              Resources resources) {
        super(containerService, meterRegistry, resources);
        this.containerService = containerService;
        this.meterRegistry = meterRegistry;
    }
    
    @PostConstruct
    public void init() {
        compilationTimer = meterRegistry.timer(WellKnownMetrics.COMPILATION_TIMER, "compiler", "compilation");
        executionTimer = meterRegistry.timer(WellKnownMetrics.EXECUTION_TIMER, "compiler", "execution");
    }
    
    @Override
    public CompilationResponse compile(Execution execution) {
        
        // repository name must be lowercase
        String compilationImageName = IMAGE_PREFIX_NAME + execution.getLanguage().toString().toLowerCase();
    
        // If the app is running inside a container, we should share the same volume with the compilation container.
        final String volume = compilationContainerVolume.isEmpty()
                                    ? System.getProperty("user.dir")
                                    : compilationContainerVolume;
    
        String sourceCodeFileName = execution.getSourceCodeFile().getOriginalFilename();
    
        String containerName = COMPILATION_CONTAINER_NAME_PREFIX + execution.getImageName();
    
        var processOutput = new AtomicReference<ProcessOutput>();
        compilationTimer.record(() -> {
            processOutput.set(
                    compile(volume, compilationImageName, containerName, execution.getPath(), sourceCodeFileName)
            );
        });
    
        ProcessOutput compilationOutput = processOutput.get();
        
        int compilationDuration = compilationOutput.getExecutionDuration();
    
        ContainerInfo containerInfo = containerService.inspect(containerName);
    
        ContainerHelper.logContainerInfo(containerName, containerInfo);
    
        compilationDuration = ContainerHelper.getExecutionDuration(
                                                    containerInfo == null ? null : containerInfo.getStartTime(),
                                                    containerInfo == null ? null : containerInfo.getEndTime(),
                                                    compilationDuration);
        
        Verdict verdict;
        
        switch (compilationOutput.getStatus()) {
            case StatusUtils.ACCEPTED_OR_WRONG_ANSWER_STATUS:
                log.info("Compilation succeeded!");
                verdict = Verdict.ACCEPTED;
                break;
            case StatusUtils.TIME_LIMIT_EXCEEDED_STATUS:
                log.warn("Time limit exceeded during compilation step, error: {}", compilationOutput.getStdErr());
                throw new CompilationTimeoutException("Timeout during compilation step, please retry again");
            case StatusUtils.OUT_OF_MEMORY_STATUS:
                // TODO: set memory limit to use inside the container
                log.warn("The compilation step exceeded the maximum allowed memory, error: {}", compilationOutput.getStdErr());
                throw new ResourceLimitReachedException("The compilation step exceeded the maximum allowed memory");
            default:
                log.info("Compilation error!");
                verdict = Verdict.COMPILATION_ERROR;
        }
        
        ContainerHelper.deleteContainer(containerName, containerService, threadPool);
        
        return CompilationResponse
                .builder()
                .verdict(verdict)
                .error(compilationOutput.getStdErr())
                .compilationDuration(compilationDuration)
                .build();
    }
    
    private ProcessOutput compile(String volume,
                                  String imageName,
                                  String containerName,
                                  String executionPath,
                                  String sourceCodeFileName) {
        String volumeMounting = volume + ":" + EXECUTION_PATH_INSIDE_CONTAINER;
        return containerService.runContainer(
                imageName,
                containerName,
                COMPILATION_TIME_OUT,
                volumeMounting,
                executionPath,
                sourceCodeFileName);
    }
}
