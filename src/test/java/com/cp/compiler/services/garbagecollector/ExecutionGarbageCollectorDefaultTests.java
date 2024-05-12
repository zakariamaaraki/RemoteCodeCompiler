package com.cp.compiler.services.garbagecollector;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.repositories.executions.ExecutionRepository;
import com.cp.compiler.services.platform.containers.ContainerService;
import com.cp.compiler.services.platform.garbagecollector.ExecutionGarbageCollector;
import com.cp.compiler.services.platform.garbagecollector.ExecutionGarbageCollectorDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExecutionGarbageCollectorDefaultTests {

    private ExecutionRepository executionRepository;
    private ContainerService containerService;
    private ExecutionGarbageCollector garbageCollector;

    @BeforeEach
    void setUp() {
        executionRepository = Mockito.mock(ExecutionRepository.class);
        containerService = Mockito.mock(ContainerService.class);
        garbageCollector = new ExecutionGarbageCollectorDefault(executionRepository, containerService);
    }

    @Test
    void collectExecutions_shouldDeleteOldExecutions() {
        // Given
        Execution oldExecution = createMockExecution(LocalDateTime.now().minusMinutes(11));
        Execution newExecution = createMockExecution(LocalDateTime.now().minusMinutes(5));
        List<Execution> executions = Arrays.asList(oldExecution, newExecution);

        // Mock the executionRepository.getExecutions() to return the list of executions
        when(executionRepository.getExecutions()).thenReturn(executions);

        // Capture the UUID of the old execution
        String oldExecutionId = oldExecution.getId();

        // When
        garbageCollector.collectExecutions();

        // Then
        verify(containerService, times(oldExecution.getTestCases().size())).deleteContainer(anyString());
        verify(executionRepository).removeExecution(eq(oldExecutionId)); // Use the captured UUID
        verify(executionRepository, never()).removeExecution(eq(newExecution.getId()));

        // Capture the IDs of the deleted containers
        ArgumentCaptor<String> containerNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(containerService).deleteContainer(containerNameCaptor.capture());
        assertEquals(oldExecution.getTestCaseContainerName(oldExecution.getTestCases().get(0).getTestCaseId()),
                containerNameCaptor.getAllValues().get(0));
    }

    @Test
    void collectExecutions_shouldNotDeleteNewExecutions() {
        // Given
        Execution newExecution = createMockExecution(LocalDateTime.now().minusMinutes(5));
        when(executionRepository.getExecutions()).thenReturn(List.of(newExecution));

        // When
        garbageCollector.collectExecutions();

        // Then
        verify(containerService, never()).deleteContainer(anyString());
        verify(executionRepository, never()).removeExecution(anyString());
    }

    @Test
    void collectExecutions_shouldHandleEmptyExecutionsList() {
        // Given
        when(executionRepository.getExecutions()).thenReturn(List.of());

        // When
        garbageCollector.collectExecutions();

        // Then
        verify(containerService, never()).deleteContainer(anyString());
        verify(executionRepository, never()).removeExecution(anyString());
    }

    private Execution createMockExecution(LocalDateTime dateTime) {
        Execution execution = Mockito.mock(Execution.class);
        TransformedTestCase testCase = Mockito.mock(TransformedTestCase.class);
        String executionId = UUID.randomUUID().toString(); // Capture the execution ID
        String testCaseId = UUID.randomUUID().toString();
        String containerName = "execution-container-" + testCaseId + "-imageName";

        when(execution.getDateTime()).thenReturn(dateTime);
        when(execution.getId()).thenReturn(executionId); // Use the captured execution ID
        when(execution.getTestCases()).thenReturn(List.of(testCase));
        when(testCase.getTestCaseId()).thenReturn(testCaseId);
        when(execution.getTestCaseContainerName(testCaseId)).thenReturn(containerName);

        return execution;
    }
}
