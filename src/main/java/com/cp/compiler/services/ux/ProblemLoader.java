package com.cp.compiler.services.ux;

import com.cp.compiler.contract.problems.Problem;

import java.util.List;

public interface ProblemLoader {
    
    Problem getProblemById(long problemId);
    
    List<Problem> getAllProblems();
}
