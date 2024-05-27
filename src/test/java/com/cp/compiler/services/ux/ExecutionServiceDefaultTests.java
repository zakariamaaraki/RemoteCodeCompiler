package com.cp.compiler.services.ux;

import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.contract.problems.Difficulty;
import com.cp.compiler.contract.problems.Problem;
import com.cp.compiler.contract.problems.ProblemExecution;
import com.cp.compiler.contract.testcases.TestCase;
import com.cp.compiler.contract.testcases.TestCaseResult;
import com.cp.compiler.executions.ExecutionFactory;
import com.cp.compiler.executions.ExecutionType;
import com.cp.compiler.executions.languages.JavaExecution;
import com.cp.compiler.models.Verdict;
import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.repositories.executions.ExecutionRepositoryDefault;
import com.cp.compiler.repositories.problems.ProblemsRepository;
import com.cp.compiler.services.api.CompilerFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExecutionServiceDefaultTests {
    
    @Mock
    private CompilerFacade compiler;
    
    @Mock
    private ProblemsRepository problemsRepository;
    
    @InjectMocks
    private ExecutionServiceDefault executionService;

    @BeforeEach
    void setUp() {
        ExecutionFactory.registerExecution(
                Language.JAVA,
                (MultipartFile sourceCode, List<TransformedTestCase> testCases, int timeLimit, int memoryLimit) -> new JavaExecution(
                        sourceCode,
                        testCases,
                        timeLimit,
                        memoryLimit));

        ExecutionFactory.registerExecutionType(Language.JAVA, new ExecutionType(null, null, new ExecutionRepositoryDefault()));
    }
    
    @Test
    void execute_ValidProblem_ReturnsCompilerResponse() throws IOException {
        // Given
        long problemId = 1L;
        Problem problem = createSampleProblem(problemId);
        ProblemExecution problemExecution = createSampleProblemExecution(problemId);
        RemoteCodeCompilerRequest expectedRequest = createSampleCompilerRequest(problem, problemExecution);
        
        when(problemsRepository.getProblemById(problemId)).thenReturn(problem);
        when(compiler.compile(any(JavaExecution.class), eq(false), eq(null), eq("rcc-ux")))
                .thenReturn(createSampleCompilerResponse(expectedRequest));
        
        // When
        ResponseEntity<RemoteCodeCompilerResponse> responseEntity = executionService.execute(problemExecution);
        
        // Then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        RemoteCodeCompilerResponse compilerResponse = responseEntity.getBody();
        assertNotNull(compilerResponse);
        assertEquals(expectedRequest.getLanguage(), compilerResponse.getExecution().getLanguage());
        assertEquals(expectedRequest.getTimeLimit(), compilerResponse.getExecution().getTimeLimit());
        assertEquals(expectedRequest.getMemoryLimit(), compilerResponse.getExecution().getMemoryLimit());
        assertEquals(Verdict.ACCEPTED.getStatusResponse(), compilerResponse.getExecution().getVerdict());
        
        Map<String, TestCaseResult> testCasesResult = compilerResponse.getExecution().getTestCasesResult();
        assertNotNull(testCasesResult);
        assertEquals(2, testCasesResult.size());
        
        int index = 0;
        for (TestCaseResult testCaseResult : testCasesResult.values()) {
            if (index++ < 2) {
                assertNotNull(testCaseResult.getExpectedOutput());
            } else {
                assertEquals("**Hidden**", testCaseResult.getExpectedOutput());
                assertEquals("**Hidden**", testCaseResult.getOutputDiff());
            }
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
    
    private ProblemExecution createSampleProblemExecution(long problemId) {
        return new ProblemExecution(
                problemId,
                "Sample source code",
                Language.JAVA);
    }
    
    private RemoteCodeCompilerRequest createSampleCompilerRequest(Problem problem, ProblemExecution problemExecution) {
        LinkedHashMap<String, TestCase> testCases = new LinkedHashMap<>();
        int index = 0;
        
        for (TestCase testCase : problem.getTestCases()) {
            testCases.put(String.valueOf(index++), testCase);
        }
        
        return new RemoteCodeCompilerRequest(
                problemExecution.getSourceCode(),
                problemExecution.getLanguage(),
                problem.getTimeLimit(),
                problem.getMemoryLimit(),
                testCases);
    }
    
    private ResponseEntity<RemoteCodeCompilerResponse> createSampleCompilerResponse(RemoteCodeCompilerRequest compilerRequest) {
        var execution =  new RemoteCodeCompilerExecutionResponse();
        execution.setLanguage(compilerRequest.getLanguage());
        execution.setTimeLimit(compilerRequest.getTimeLimit());
        execution.setMemoryLimit(compilerRequest.getMemoryLimit());
        execution.setVerdict(Verdict.ACCEPTED.getStatusResponse());
    
        // Create two sample TestCaseResult instances
        TestCaseResult testCaseResult1 = new TestCaseResult(
                Verdict.ACCEPTED,
                "Sample Output 1",
                null,
                "Expected Output 1",
                1000
        );
    
        TestCaseResult testCaseResult2 = new TestCaseResult(
                Verdict.WRONG_ANSWER,
                "Sample Output 2",
                null,
                "Expected Output 2",
                1500
        );
    
        execution.setTestCasesResult(new LinkedHashMap<>());
        execution.getTestCasesResult().put("0", testCaseResult1);
        execution.getTestCasesResult().put("1", testCaseResult2);
        
        RemoteCodeCompilerResponse compilerResponse = new RemoteCodeCompilerResponse();
        compilerResponse.setExecution(execution);
    
        return new ResponseEntity<>(compilerResponse, HttpStatus.OK);
    }
}
