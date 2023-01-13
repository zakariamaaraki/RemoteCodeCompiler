package com.cp.compiler.services.businesslogic;

import com.cp.compiler.exceptions.CompilerServerInternalException;
import com.cp.compiler.exceptions.ContainerOperationTimeoutException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.CompilationResponse;
import com.cp.compiler.models.ExecutionResponse;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.models.containers.ContainerInfo;
import com.cp.compiler.models.processes.ProcessOutput;
import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.testcases.TestCaseResult;
import com.cp.compiler.services.containers.ContainerService;
import com.cp.compiler.services.resources.Resources;
import com.cp.compiler.utils.CmdUtils;
import com.cp.compiler.utils.StatusUtils;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public abstract class ExecutionStrategy {
    
    protected Timer executionTimer;
    
    private final Map<String, Counter> verdictsCounters = new HashMap<>();
    
    private final ContainerService containerService;
    
    protected final ExecutorService threadPool;
    
    private static final long EXECUTION_TIME_OUT = 20000; // in ms
    
    private final Resources resources;
    
    /**
     * The execution container name prefix
     */
    private static final String EXECUTION_CONTAINER_NAME_PREFIX = "execution-";
    
    protected ExecutionStrategy(ContainerService containerService,
                                MeterRegistry meterRegistry,
                                Resources resources) {
        this.containerService = containerService;
        this.threadPool = Executors.newCachedThreadPool();
        this.resources = resources;
    
        // Init verdict counter
        Arrays.stream(Verdict.values())
                .forEach(verdict -> verdictsCounters.put(verdict.getStatusResponse(),
                        meterRegistry.counter(verdict.getCounterMetric())));
    }
    
    public abstract CompilationResponse compile(Execution execution);
    
    public ExecutionResponse run(Execution execution, boolean deleteImageAfterExecution) {
    
        var testCasesResult = new LinkedHashMap<String, TestCaseResult>();
        Verdict verdict = null;
        String err = "";
    
        for (ConvertedTestCase testCase : execution.getTestCases()) {
    
            TestCaseResult testCaseResult = executeTestCase(execution, deleteImageAfterExecution, testCase);
    
            testCasesResult.put(testCase.getTestCaseId(), testCaseResult);
            
            verdict = testCaseResult.getVerdict();
        
            log.info("Status response for the test case {} is {}",
                    testCase.getTestCaseId(),
                    verdict.getStatusResponse());
        
            // Update metrics
            verdictsCounters.get(verdict.getStatusResponse()).increment();
        
            if (verdict != Verdict.ACCEPTED) {
                // Don't continue if the current test case failed
                log.info("Test case id: {} failed, abort executions", testCase.getTestCaseId());
                err = testCaseResult.getError();
                break;
            }
        }
        
        return ExecutionResponse
                .builder()
                .verdict(verdict)
                .testCasesResult(testCasesResult)
                .error(err)
                .build();
    }
    
    private TestCaseResult executeTestCase(Execution execution,
                                           boolean deleteImageAfterExecution,
                                           ConvertedTestCase testCase) {
        
        // Create an entrypoint file for the current execution
        execution.createEntrypointFile(
                testCase.getInputFile() == null
                        ? null
                        : testCase.getInputFile().getOriginalFilename());
        
        String expectedOutput = getExpectedOutput(testCase.getExpectedOutputFile());
        
        // Free memory space
        testCase.freeMemorySpace();
        
        // Build the execution container image with the created entrypoint file
        containerService.buildImage(
                execution.getPath(),
                execution.getImageName(),
                WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
        var result = new AtomicReference<TestCaseResult>();
        executionTimer.record(() -> {
            // Run the execution container
            result.set(runContainer(execution, testCase.getTestCaseId(), expectedOutput));
        });
        
        // Delete container image asynchronously
        if (deleteImageAfterExecution) {
            ContainerHelper.deleteImage(execution.getImageName(), containerService, threadPool);
        }
        
        TestCaseResult testCaseResult = result.get();
        return testCaseResult;
    }
    
    private String getExpectedOutput(MultipartFile outputFile) {
        try {
            var expectedOutputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
            return CmdUtils.readOutput(expectedOutputReader);
        } catch (Exception exception) {
            log.error("Unexpected error while reading the expected output file: {}", exception);
            throw new CompilerServerInternalException("Unexpected error while reading the expected output file");
        }
    }
    
    private TestCaseResult runContainer(Execution execution, String testCaseId, String expectedOutput) {
        
        String containerName = getExecutionContainerName(execution.getImageName(), testCaseId);
        
        try {
            log.info("Start running the container: {}", containerName);
            ProcessOutput containerOutput = containerService.runContainer(
                    execution.getImageName(),
                    containerName,
                    EXECUTION_TIME_OUT,
                    resources.getMaxCpus());
            
            Verdict verdict = getVerdict(containerOutput, expectedOutput);
            
            ContainerHelper.cleanStdErrOutput(containerOutput, execution);
            
            // Inspect the container to get info about it
            ContainerInfo containerInfo =  containerService.inspect(containerName);
            ContainerHelper.logContainerInfo(containerName, containerInfo);
            
            int executionDuration = ContainerHelper.getExecutionDuration(
                    containerInfo == null ? null : containerInfo.getStartTime(),
                    containerInfo == null ? null : containerInfo.getEndTime(),
                    containerOutput.getExecutionDuration());
            
            return new TestCaseResult(
                    verdict,
                    containerOutput.getStdOut(),
                    containerOutput.getStdErr(),
                    expectedOutput,
                    executionDuration);
            
        } catch(ContainerOperationTimeoutException exception) {
            // Should be caught inside the container
            log.warn("Tme limit exceeded during the execution: {}", exception);
            
            ContainerInfo containerInfo =  containerService.inspect(containerName);
            ContainerHelper.logContainerInfo(containerName, containerInfo);
            
            return new TestCaseResult(
                    Verdict.TIME_LIMIT_EXCEEDED,
                    "",
                    "The execution exceeded the time limit",
                    expectedOutput,
                    execution.getTimeLimit() + 1);
        } finally {
            ContainerHelper.deleteContainer(containerName, containerService, threadPool);
        }
    }
    
    private String getExecutionContainerName(String imageName, String testCaseId) {
        return EXECUTION_CONTAINER_NAME_PREFIX + testCaseId + "-" + imageName;
    }
    
    private Verdict getVerdict(ProcessOutput containerOutput, String expectedOutput) {
        boolean result = CmdUtils.compareOutput(containerOutput.getStdOut(), expectedOutput);
        return StatusUtils.statusResponse(containerOutput.getStatus(), result);
    }
}
