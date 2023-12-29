package com.cp.compiler.controllers;

import com.cp.compiler.contract.problems.Problem;
import com.cp.compiler.services.ux.ProblemLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ProblemController {
    
    private ProblemLoader problemLoader;
    
    public ProblemController(ProblemLoader problemLoader) {
        this.problemLoader = problemLoader;
    }
    
    @GetMapping("/problem/{problemId}")
    public String getProblemWebPage(Model model, @PathVariable long problemId) {
        Problem problem = problemLoader.getProblemById(problemId);
        model.addAttribute("problem", problem);
        return "problem.html";
    }
}
