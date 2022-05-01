package com.cp.compiler.controllers;

import com.cp.compiler.exceptions.CompilerServerInternalException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.*;
import com.cp.compiler.services.CompilerFacade;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Compiler Controller Class, this class exposes 4 endpoints for (Java, C, CPP, and Python)
 *
 * @author Zakaria Maaraki
 */

@RestController
@RequestMapping("/compiler")
public class CompilerController {
    
    private CompilerFacade compiler;
    
    public CompilerController(CompilerFacade compiler) {
        this.compiler = compiler;
    }
    
    /**
     * Take as a parameter a json object
     *
     * @param request object
     * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Compilation Error, RunTime Error)
     * @throws CompilerServerInternalException The compiler exception
     */
    @PostMapping("/json")
    @ApiOperation(
            value = "json",
            notes = "You should provide outputFile, inputFile (not required), source code, time limit and memory limit",
            response = Response.class
    )
    public ResponseEntity<Object> compile(@ApiParam(value = "request") @RequestBody Request request,
                                          @RequestHeader(value = WellKnownParams.PREFER_PUSH, required = false) String preferPush,
                                          @RequestHeader(value = WellKnownParams.URL, required = false) String url)
            throws Exception {
        
        Execution execution = ExecutionFactory.createExecution(
                request.getSourceCode(),
                request.getInput(),
                request.getExpectedOutput(),
                request.getTimeLimit(),
                request.getMemoryLimit(),
                Language.PYTHON);
        
        boolean isLongRunning = WellKnownHeaders.PREFER_PUSH.equals(preferPush);
        return compiler.compile(execution, isLongRunning, url);
    }
    
    /**
     * Python Compiler Controller
     *
     * @param outputFile  Expected output
     * @param sourceCode  Python source code
     * @param inputFile   Input data (optional)
     * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
     * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
     * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Compilation Error, RunTime Error)
     * @throws CompilerServerInternalException The compiler exception
     */
    @PostMapping("/python")
    @ApiOperation(
            value = "Python compiler",
            notes = "You should provide outputFile, inputFile (not required), source code, time limit and memory limit",
            response = Response.class
    )
    public ResponseEntity compilePython(
            @ApiParam(value = "The expected output")
            @RequestPart(value = WellKnownParams.OUTPUT_FILE) MultipartFile outputFile,
            
            @ApiParam(value = "Your source code")
            @RequestPart(value = WellKnownParams.SOURCE_CODE) MultipartFile sourceCode,
            
            @ApiParam(value = "This one is not required, it's just the inputs")
            @RequestParam(value = WellKnownParams.INPUT_FILE, required = false) MultipartFile inputFile,
            
            @ApiParam(value = "The time limit that the execution must not exceed")
            @RequestParam(value = WellKnownParams.TIME_LIMIT) int timeLimit,
            
            @ApiParam(value = "The memory limit that the running program must not exceed")
            @RequestParam(value = WellKnownParams.MEMORY_LIMIT) int memoryLimit,

            @RequestHeader(value = WellKnownParams.PREFER_PUSH, required = false) String preferPush,
            @RequestHeader(value = WellKnownParams.URL, required = false) String url) throws Exception {
        
        Execution execution = ExecutionFactory.createExecution(
                sourceCode, inputFile, outputFile, timeLimit, memoryLimit, Language.PYTHON);
        
        boolean isLongRunning = WellKnownHeaders.PREFER_PUSH.equals(preferPush);
        return compiler.compile(execution, isLongRunning, url);
    }
    
    /**
     * C Compiler Controller
     *
     * @param outputFile  Expected output
     * @param sourceCode  C source code
     * @param inputFile   Input data (optional)
     * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
     * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
     * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded,
     * Compilation Error, RunTime Error)
     * @throws CompilerServerInternalException the compiler exception
     */
    @PostMapping("/c")
    @ApiOperation(
            value = "C compiler",
            notes = "You should provide outputFile, inputFile (not required), source code, time limit and memory limit",
            response = Response.class
    )
    public ResponseEntity compileC(
            @ApiParam(value = "The expected output")
            @RequestPart(value = WellKnownParams.OUTPUT_FILE) MultipartFile outputFile,
            
            @ApiParam(value = "Your source code")
            @RequestPart(value = WellKnownParams.SOURCE_CODE) MultipartFile sourceCode,
            
            @ApiParam(value = "This one is not required, it's just the inputs")
            @RequestParam(value = WellKnownParams.INPUT_FILE, required = false) MultipartFile inputFile,
            
            @ApiParam(value = "The time limit that the execution must not exceed")
            @RequestParam(value = WellKnownParams.TIME_LIMIT) int timeLimit,
            
            @ApiParam(value = "The memory limit that the running program must not exceed")
            @RequestParam(value = WellKnownParams.MEMORY_LIMIT) int memoryLimit,
            
            @RequestHeader(value = WellKnownParams.PREFER_PUSH, required = false) String preferPush,
            @RequestHeader(value = WellKnownParams.URL, required = false) String url) throws Exception {
        
        Execution execution = ExecutionFactory.createExecution(
                sourceCode, inputFile, outputFile, timeLimit, memoryLimit, Language.C);
    
        boolean isLongRunning = WellKnownHeaders.PREFER_PUSH.equals(preferPush);
        return compiler.compile(execution, isLongRunning, url);
    }
    
    /**
     * C++ Compiler Controller
     *
     * @param outputFile  Expected output
     * @param sourceCode  C++ source code
     * @param inputFile   Input data (optional)
     * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
     * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
     * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded,
     * Compilation Error, RunTime Error)
     * @throws CompilerServerInternalException the compiler exception
     */
    @PostMapping("/cpp")
    @ApiOperation(
            value = "Cpp compiler",
            notes = "You should provide outputFile, inputFile (not required), source code, time limit and memory limit",
            response = Response.class
    )
    public ResponseEntity compileCpp(
            @ApiParam(value = "The expected output")
            @RequestPart(value = WellKnownParams.OUTPUT_FILE) MultipartFile outputFile,
            
            @ApiParam(value = "Your source code")
            @RequestPart(value = WellKnownParams.SOURCE_CODE) MultipartFile sourceCode,
            
            @ApiParam(value = "This one is not required, it's just the inputs")
            @RequestParam(value = WellKnownParams.INPUT_FILE, required = false) MultipartFile inputFile,
            
            @ApiParam(value = "The time limit that the execution must not exceed")
            @RequestParam(value = WellKnownParams.TIME_LIMIT) int timeLimit,
            
            @ApiParam(value = "The memory limit that the running program must not exceed")
            @RequestParam(value = WellKnownParams.MEMORY_LIMIT) int memoryLimit,

            @RequestHeader(value = WellKnownParams.PREFER_PUSH, required = false) String preferPush,
            @RequestHeader(value = WellKnownParams.URL, required = false) String url) throws Exception {
        
        Execution execution = ExecutionFactory.createExecution(
                sourceCode, inputFile, outputFile, timeLimit, memoryLimit, Language.CPP);
    
        boolean isLongRunning = WellKnownHeaders.PREFER_PUSH.equals(preferPush);
        return compiler.compile(execution, isLongRunning, url);
    }
    
    /**
     * Java Compiler Controller
     *
     * @param outputFile  Expected output
     * @param sourceCode  Java source code
     * @param inputFile   Input data (optional)
     * @param timeLimit   Time limit of the execution, must be between 0 and 15 sec
     * @param memoryLimit Memory limit of the execution, must be between 0 and 1000 MB
     * @return The verdict of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded,
     * Compilation Error, RunTime Error)
     * @throws CompilerServerInternalException the compiler exception
     */
    @PostMapping("/java")
    @ApiOperation(
            value = "Java compiler",
            notes = "You should provide outputFile, inputFile (not required), source code, time limit and memory limit",
            response = Response.class
    )
    public ResponseEntity compileJava(
            @ApiParam(value = "The expected output")
            @RequestPart(value = WellKnownParams.OUTPUT_FILE) MultipartFile outputFile,
            
            @ApiParam(value = "Your source code")
            @RequestPart(value = WellKnownParams.SOURCE_CODE) MultipartFile sourceCode,
            
            @ApiParam(value = "This one is not required, it's just the inputs")
            @RequestParam(value = WellKnownParams.INPUT_FILE, required = false) MultipartFile inputFile,
            
            @ApiParam(value = "The time limit that the execution must not exceed")
            @RequestParam(value = WellKnownParams.TIME_LIMIT) int timeLimit,
            
            @ApiParam(value = "The memory limit that the running program must not exceed")
            @RequestParam(value = WellKnownParams.MEMORY_LIMIT) int memoryLimit,

            @RequestHeader(value = WellKnownParams.PREFER_PUSH, required = false) String preferPush,
            @RequestHeader(value = WellKnownParams.URL, required = false) String url) throws Exception {
        
        Execution execution = ExecutionFactory.createExecution(
                sourceCode, inputFile, outputFile, timeLimit, memoryLimit, Language.JAVA);
    
        boolean isLongRunning = WellKnownHeaders.PREFER_PUSH.equals(preferPush);
        return compiler.compile(execution, isLongRunning, url);
    }
    
}
