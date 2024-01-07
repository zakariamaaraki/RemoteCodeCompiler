package com.cp.compiler.services.ux;

import com.cp.compiler.contract.problems.Difficulty;
import com.cp.compiler.contract.problems.Problem;
import com.cp.compiler.contract.testcases.TestCase;
import com.cp.compiler.exceptions.problems.ProblemNotFoundException;
import com.cp.compiler.repositories.problems.ProblemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ProblemLoaderTests {
    
    @Mock
    private ProblemsRepository problemsRepository;
    
    @InjectMocks
    private ProblemLoaderDefault problemLoader;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    void getProblemById_ExistingProblem_ReturnsTransformedProblem() {
        // Arrange
        long problemId = 1L;
        Problem originalProblem = createSampleProblem(problemId);
        when(problemsRepository.getProblemById(problemId)).thenReturn(originalProblem);
        
        // Act
        Problem transformedProblem = problemLoader.getProblemById(problemId);
        
        // Assert
        assertNotSame(originalProblem, transformedProblem);
        assertEquals(originalProblem.getId(), transformedProblem.getId());
        assertEquals(originalProblem.getTitle(), transformedProblem.getTitle());
        assertEquals(originalProblem.getDescription(), transformedProblem.getDescription());
        assertEquals(originalProblem.getDifficulty(), transformedProblem.getDifficulty());
        assertEquals(originalProblem.getTags(), transformedProblem.getTags());
        assertEquals(originalProblem.getTestCases().subList(0, 2), transformedProblem.getTestCases());
        assertEquals(originalProblem.getTimeLimit(), transformedProblem.getTimeLimit());
        assertEquals(originalProblem.getMemoryLimit(), transformedProblem.getMemoryLimit());
    }
    
    @Test
    void getProblemById_NonExistingProblem_ThrowsProblemNotFoundException() {
        // Arrange
        long nonExistingProblemId = 99L;
        when(problemsRepository.getProblemById(nonExistingProblemId))
                .thenThrow(new ProblemNotFoundException("Problem not found"));
        
        // Act and Assert
        assertThrows(ProblemNotFoundException.class, () -> problemLoader.getProblemById(nonExistingProblemId));
    }
    
    @Test
    void getAllProblems_ReturnsTransformedProblemsList() {
        // Arrange
        List<Problem> originalProblems =
                Arrays.asList(createSampleProblem(1L), createSampleProblem(2L));
        when(problemsRepository.getAllProblems()).thenReturn(originalProblems);
        
        // Act
        List<Problem> transformedProblems = problemLoader.getAllProblems();
        
        // Assert
        assertNotSame(originalProblems, transformedProblems);
        assertEquals(originalProblems.size(), transformedProblems.size());
        
        for (int i = 0; i < originalProblems.size(); i++) {
            Problem originalProblem = originalProblems.get(i);
            Problem transformedProblem = transformedProblems.get(i);
            
            assertEquals(originalProblem.getId(), transformedProblem.getId());
            assertEquals(originalProblem.getTitle(), transformedProblem.getTitle());
            assertEquals(originalProblem.getDescription(), transformedProblem.getDescription());
            assertEquals(originalProblem.getDifficulty(), transformedProblem.getDifficulty());
            assertEquals(originalProblem.getTags(), transformedProblem.getTags());
            assertEquals(originalProblem.getTestCases().subList(0, 2), transformedProblem.getTestCases());
            assertEquals(originalProblem.getTimeLimit(), transformedProblem.getTimeLimit());
            assertEquals(originalProblem.getMemoryLimit(), transformedProblem.getMemoryLimit());
        }
    }
    
    private Problem createSampleProblem(long problemId) {
        return Problem.builder()
                .id(problemId)
                .title("Sample Problem " + problemId)
                .description("Description of Sample Problem " + problemId)
                .difficulty(Difficulty.MEDIUM)
                .tags(Arrays.asList("tag1", "tag2"))
                .testCases(Arrays.asList(
                        new TestCase("input1", "output1"),
                        new TestCase("input2", "output2"),
                        new TestCase("input3", "output3")
                ))
                .timeLimit(1000)
                .memoryLimit(256)
                .build();
    }
}