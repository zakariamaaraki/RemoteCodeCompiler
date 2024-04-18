package com.cp.compiler.api.controllers;

import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.exceptions.CompilerServerInternalException;
import com.cp.compiler.executions.Execution;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.services.api.CompilerFacade;
import com.cp.compiler.utils.CmdUtils;
import com.cp.compiler.consts.WellKnownHeaders;
import com.cp.compiler.consts.WellKnownParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Compiler Controller Class
 *
 * @author Zakaria Maaraki
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CompilerController {
    
    private CompilerFacade compiler;
    
    /**
     * Instantiates a new Compiler controller.
     *
     * @param compiler the compiler
     */
    public CompilerController(CompilerFacade compiler) {
        this.compiler = compiler;
    }
    
    /**
     * Execute a source code against multiple test cases.
     *
     * @param request json object
     * @param userId  the user id
     * @param prefer  the prefer operation (currently there is only prefer-push which is optional)
     * @param url     if the prefer is set to prefer-push the url where the response should be sent should be specified
     * @return The statusResponse of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Compilation Error, RunTime Error)
     * @throws IOException the io exception
     */
    @PostMapping("/compile/json")
    @ApiOperation(
            value = "Json",
            notes = "You should provide outputFile, inputFile (not required), source code, time limit and memory limit",
            response = RemoteCodeCompilerExecutionResponse.class
    )
    public ResponseEntity<RemoteCodeCompilerResponse> compile(@ApiParam(value = "request") @RequestBody RemoteCodeCompilerRequest request,
                                                              @RequestHeader(value = WellKnownParams.USER_ID, required = false) String userId,
                                                              @RequestHeader(value = WellKnownParams.PREFER, required = false) String prefer,
                                                              @RequestHeader(value = WellKnownParams.URL, required = false) String url)
            throws IOException {
        
        Execution execution = ExecutionFactory.createExecution(
                request.getSourcecodeFile(),
                request.getConvertedTestCases(),
                request.getTimeLimit(),
                request.getMemoryLimit(),
                request.getLanguage());
        
        // Free memory space, the request could take so much time
        request = null;
        
        boolean isLongRunning = WellKnownHeaders.PREFER_PUSH.equals(prefer);
    
        return compiler.compile(execution, isLongRunning, url, userId);
    }
    
    /**
     * Compiles a source code against multiple test cases
     *
     * @param language        the programming language
     * @param sourceCode      the source code
     * @param inputs          the inputs
     * @param expectedOutputs the expected outputs
     * @param timeLimit       Time limit of the execution, must be between 0 and 15 sec
     * @param memoryLimit     Memory limit of the execution, must be between 0 and 1000 MB
     * @param prefer          the prefer push
     * @param url             the url
     * @param userId          the user id
     * @return The statusResponse of the execution (Accepted, Wrong Answer, Time Limit Exceeded, Memory Limit Exceeded, Compilation Error, RunTime Error)
     * @throws IOException the io exception
     */
    @PostMapping("/compile")
    @ApiOperation(
            value = "Multipart request",
            notes = "You should provide outputFile, inputFile (not required), source code, time limit and memory limit "
                    + "and the language",
            response = RemoteCodeCompilerExecutionResponse.class
    )
    public ResponseEntity<RemoteCodeCompilerResponse> compile(
            @ApiParam(value = "The language")
            @RequestParam(value = WellKnownParams.LANGUAGE) Language language,
        
            @ApiParam(value = "Your source code")
            @RequestPart(value = WellKnownParams.SOURCE_CODE) MultipartFile sourceCode,
        
            @ApiParam(value = "Inputs")
            @RequestParam(value = WellKnownParams.INPUTS, required = false) MultipartFile inputs,

            @ApiParam(value = "Expected outputs")
            @RequestParam(value = WellKnownParams.EXPECTED_OUTPUTS) MultipartFile expectedOutputs,
        
            @ApiParam(value = "The time limit in seconds that the execution must not exceed")
            @RequestParam(value = WellKnownParams.TIME_LIMIT) int timeLimit,
        
            @ApiParam(value = "The memory limit in MB that the execution must not exceed")
            @RequestParam(value = WellKnownParams.MEMORY_LIMIT) int memoryLimit,
        
            @RequestHeader(value = WellKnownParams.PREFER, required = false) String prefer,
            
            @RequestHeader(value = WellKnownParams.URL, required = false) String url,

            @RequestHeader(value = WellKnownParams.USER_ID, required = false) String userId)
            
            throws IOException {
        
        TransformedTestCase testCase =
                new TransformedTestCase("defaultTestId", inputs, getExpectedOutput(expectedOutputs));
        
        Execution execution = ExecutionFactory.createExecution(
                sourceCode,
                List.of(testCase),
                timeLimit,
                memoryLimit,
                language);
        
        boolean isLongRunning = WellKnownHeaders.PREFER_PUSH.equals(prefer);
    
        return compiler.compile(execution, isLongRunning, url, userId);
    }
    
    private String getExpectedOutput(MultipartFile outputFile) {
        try {
            var expectedOutputReader = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
            return CmdUtils.readOutput(expectedOutputReader);
        } catch (Exception exception) {
            log.error("Unexpected error while reading the expected output file: {}", exception);
            throw new CompilerServerInternalException("Unexpected error while reading the expected output file");
        }
    }
}
