package com.cp.compiler.repositories.problems;

import com.cp.compiler.contract.problems.Problem;

import java.util.List;

public interface ProblemsRepository {

    Problem getProblemById(long problemId);
    
    List<Problem> getAllProblems();
}
