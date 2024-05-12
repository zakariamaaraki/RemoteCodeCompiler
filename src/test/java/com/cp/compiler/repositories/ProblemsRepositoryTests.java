package com.cp.compiler.repositories;

import com.cp.compiler.contract.problems.Difficulty;
import com.cp.compiler.contract.problems.Problem;
import com.cp.compiler.exceptions.problems.InvalidProblemException;
import com.cp.compiler.exceptions.problems.ProblemNotFoundException;
import com.cp.compiler.repositories.problems.ProblemsRepository;
import com.cp.compiler.repositories.problems.ProblemsRepositoryDefault;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProblemsRepositoryTests {
    
    @Mock
    private TypeReference<ArrayList<Problem>> typeReference;
    
    @InjectMocks
    private ProblemsRepositoryDefault problemsRepository;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(problemsRepository, "problems", new HashMap<>());
    }
    
    @Test
    void getProblemById_ExistingProblem_ReturnsProblem() {
        // Arrange
        long problemId = 1L;
        Problem expectedProblem = createSampleProblem(problemId);
        callInit(2);
        
        // Act
        Problem actualProblem = problemsRepository.getProblemById(problemId);
        
        // Assert
        assertEquals(expectedProblem, actualProblem);
    }
    
    @Test
    void getProblemById_NonExistingProblem_ThrowsProblemNotFoundException() {
        // Arrange
        long nonExistingProblemId = 99L;
        callInit(1);
        
        // Act and Assert
        assertThrows(ProblemNotFoundException.class, () -> problemsRepository.getProblemById(nonExistingProblemId));
    }
    
    @Test
    void getAllProblems_ReturnsAllProblems() {
        // Arrange
        List<Problem> expectedProblems = createSampleProblems(2);
        callInit(2);
        
        // Act
        List<Problem> actualProblems = problemsRepository.getAllProblems();
        
        // Assert
        assertEquals(expectedProblems.size(), actualProblems.size());
        
        for (int i = 0; i < actualProblems.size(); i++) {
            assertEquals(actualProblems.get(i), expectedProblems.get(i));
        }
    }
    
    @Test
    void init_ValidProblems_LoadsProblems() {
        // Act
        callInit(1);
        
        // Assert
        HashMap<Long, Problem> loadedProblems = (HashMap<Long, Problem>) ReflectionTestUtils.getField(problemsRepository, "problems");
        assertNotNull(loadedProblems);
        assertFalse(loadedProblems.isEmpty());
    }
    
    private void callInit(int problemsCount) {
        // Access the problems field using reflection and update it
        HashMap<Long, Problem> problems = (HashMap<Long, Problem>) ReflectionTestUtils.getField(problemsRepository, "problems");
        problems.clear();
        
        for (int i = 0; i < problemsCount; i++) {
            Problem createdProblem = createSampleProblem(i);
            problems.put((long)i, createdProblem);
        }
    }
    
    
    
    private Problem createSampleProblem(long problemId) {
        return Problem.builder()
                .id(problemId)
                .title("Sample Problem")
                .description("This is a sample problem.")
                .timeLimit(1000)
                .memoryLimit(256)
                .testCases(new ArrayList<>())
                .tags(List.of("tag1", "tag2"))
                .difficulty(Difficulty.MEDIUM)
                .build();
    }
    
    private List<Problem> createSampleProblems(int count) {
        List<Problem> problems = new ArrayList<>();
        for (long i = 0; i < count; i++) {
            problems.add(createSampleProblem(i));
        }
        return problems;
    }
}