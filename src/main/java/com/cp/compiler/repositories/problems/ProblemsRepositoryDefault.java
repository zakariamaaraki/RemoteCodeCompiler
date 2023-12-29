package com.cp.compiler.repositories.problems;

import com.cp.compiler.exceptions.problems.InvalidProblemException;
import com.cp.compiler.exceptions.problems.ProblemNotFoundException;
import com.cp.compiler.contract.problems.Problem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ProblemsRepositoryDefault implements ProblemsRepository {
    
    private HashMap<Long, Problem> problems = new HashMap<>();
    
    @PostConstruct
    private void init() {
        String fileName = "/problems/problems.json";
        
        // read problems file
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ArrayList<Problem>> typeReference = new TypeReference<>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream(fileName);
        try {
            List<Problem> problems = mapper.readValue(inputStream, typeReference);
            for (Problem problem : problems) {
                validate(problem);
                this.problems.put(problem.getId(), problem);
            }
            log.info("Number of problems loaded is {}", problems.size());
        } catch (IOException e){
            log.error("Error occurred while reading {} file", fileName, e);
        }
    }
    
    private void validate(Problem problem) {
        if (problem.getTestCases() == null || problem.getTestCases().size() < 2) {
            throw new InvalidProblemException("The number of test cases should be at least 2");
        }
    }
    
    @Override
    public Problem getProblemById(long problemId) {
        Problem problem = problems.get(problemId);
        if (problem == null) {
            throw new ProblemNotFoundException("A problem with id " + problemId + " does not exist");
        }
        return problem;
    }
    
    @Override
    public List<Problem> getAllProblems() {
        return problems.values().stream().collect(Collectors.toList());
    }
}
