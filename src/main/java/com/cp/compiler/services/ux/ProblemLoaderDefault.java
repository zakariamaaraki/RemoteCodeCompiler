package com.cp.compiler.services.ux;

import com.cp.compiler.contract.problems.Problem;
import com.cp.compiler.repositories.problems.ProblemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemLoaderDefault implements ProblemLoader {
    
    private ProblemsRepository problemsRepository;
    
    public ProblemLoaderDefault(ProblemsRepository problemsRepository) {
        this.problemsRepository = problemsRepository;
    }
    
    @Override
    public Problem getProblemById(long problemId) {
        return transformProblem(problemsRepository.getProblemById(problemId));
    }
    
    @Override
    public List<Problem> getAllProblems() {
        return problemsRepository
                .getAllProblems()
                .stream()
                .map(problem -> transformProblem(problem))
                .collect(Collectors.toList());
    }
    
    private Problem transformProblem(Problem problemSource) {
        return Problem
                .builder()
                .id(problemSource.getId())
                .title(problemSource.getTitle())
                .description(problemSource.getDescription())
                .difficulty(problemSource.getDifficulty())
                .tags(problemSource.getTags())
                .testCases(problemSource.getTestCases().subList(0, 2)) // Return only the first two test cases
                .timeLimit(problemSource.getTimeLimit())
                .memoryLimit(problemSource.getMemoryLimit())
                .build();
    }
}
