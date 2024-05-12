package com.cp.compiler.repositories;

import com.cp.compiler.executions.Execution;
import com.cp.compiler.repositories.executions.ExecutionRepository;
import com.cp.compiler.repositories.executions.ExecutionRepositoryDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExecutionRepositoryDefaultTests {

    private ExecutionRepository executionRepository;

    @BeforeEach
    void setUp() {
        executionRepository = new ExecutionRepositoryDefault();
    }

    @Test
    void addExecution_shouldAddExecutionToRepository() {
        // Given
        Execution execution = Mockito.mock(Execution.class);
        when(execution.getId()).thenReturn(UUID.randomUUID().toString());

        // When
        Execution addedExecution = executionRepository.addExecution(execution);

        // Then
        assertNotNull(addedExecution);
        assertEquals(execution, addedExecution);
        assertTrue(executionRepository.getExecutions().contains(execution));
    }

    @Test
    void removeExecution_shouldRemoveExecutionFromRepository() {
        // Given
        Execution execution = Mockito.mock(Execution.class);
        String executionId = UUID.randomUUID().toString();
        when(execution.getId()).thenReturn(executionId);
        executionRepository.addExecution(execution);

        // When
        Execution removedExecution = executionRepository.removeExecution(executionId);

        // Then
        assertNotNull(removedExecution);
        assertEquals(execution, removedExecution);
        assertFalse(executionRepository.getExecutions().contains(execution));
    }

    @Test
    void getExecutions_shouldReturnAllExecutions() {
        // Given
        Execution execution1 = Mockito.mock(Execution.class);
        when(execution1.getId()).thenReturn(UUID.randomUUID().toString());
        Execution execution2 = Mockito.mock(Execution.class);
        when(execution2.getId()).thenReturn(UUID.randomUUID().toString());
        executionRepository.addExecution(execution1);
        executionRepository.addExecution(execution2);

        // When
        List<Execution> executions = executionRepository.getExecutions();

        // Then
        assertNotNull(executions);
        assertEquals(2, executions.size());
        assertTrue(executions.contains(execution1));
        assertTrue(executions.contains(execution2));
    }

    @Test
    void removeExecution_shouldReturnNullIfExecutionDoesNotExist() {
        // Given
        String nonExistentExecutionId = UUID.randomUUID().toString();

        // When
        Execution removedExecution = executionRepository.removeExecution(nonExistentExecutionId);

        // Then
        assertNull(removedExecution);
    }
}
