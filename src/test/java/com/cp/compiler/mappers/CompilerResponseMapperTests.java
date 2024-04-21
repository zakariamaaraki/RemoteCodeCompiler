package com.cp.compiler.mappers;

import com.cp.compiler.contract.CompilerProto;
import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerExecutionResponse;
import com.cp.compiler.contract.RemoteCodeCompilerResponse;
import com.cp.compiler.contract.testcases.TestCaseResult;
import com.cp.compiler.mappers.grpc.CompilerResponseMapper;
import com.cp.compiler.models.Verdict;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CompilerResponseMapperTests {

    private CompilerResponseMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = Mappers.getMapper(CompilerResponseMapper.class);
    }

    @Test
    public void testToCompilerResponseProto() {
        // Create a mock response
        var executionResponse = new RemoteCodeCompilerExecutionResponse(
                "",
                100,
                "",
                new LinkedHashMap<>(),
                0,
                15,
                500,
                Language.JAVA,
                LocalDateTime.now());
        var response = new RemoteCodeCompilerResponse();
        response.setError("Error message");
        response.setExecution(executionResponse);

        // Call the mapper method
        CompilerProto.RemoteCodeCompilerResponse protoResponse = mapper.ToCompilerResponseProto(response);

        // Assertions
        assertNotNull(protoResponse);
        assertEquals("Error message", protoResponse.getError());
    }

    @Test
    public void testToCompilerExecutionResponse() {
        // Create a mock execution response
        RemoteCodeCompilerExecutionResponse executionResponse = new RemoteCodeCompilerExecutionResponse();
        executionResponse.setLanguage(Language.JAVA);
        executionResponse.setVerdict("Accepted");
        executionResponse.setStatusCode(200);
        executionResponse.setError("Execution error");
        executionResponse.setCompilationDuration(10);
        executionResponse.setTimeLimit(15);
        executionResponse.setMemoryLimit(500);
        LinkedHashMap<String, TestCaseResult> testCasesResult = new LinkedHashMap<>();
        testCasesResult.put("input1", new TestCaseResult(Verdict.ACCEPTED, "output", "error", "expectedOutput", 5));
        executionResponse.setTestCasesResult(testCasesResult);

        // Call the mapper method
        CompilerProto.RemoteCodeCompilerExecutionResponse protoExecutionResponse =
                mapper.toCompilerExecutionResponse(executionResponse);

        // Assertions
        assertNotNull(protoExecutionResponse);
        assertEquals(CompilerProto.RemoteCodeCompilerExecutionResponse.Language.JAVA, protoExecutionResponse.getLanguage());
        assertEquals("Accepted", protoExecutionResponse.getVerdict());
        assertEquals(200, protoExecutionResponse.getStatusCode());
        assertEquals("Execution error", protoExecutionResponse.getError());
        assertEquals(10, protoExecutionResponse.getCompilationDuration());
        assertEquals(15, protoExecutionResponse.getTimeLimit());
        assertEquals(500, protoExecutionResponse.getMemoryLimit());
        assertNotNull(protoExecutionResponse.getTestCasesResultMap());
        assertEquals(1, protoExecutionResponse.getTestCasesResultMap().size());
        CompilerProto.RemoteCodeCompilerExecutionResponse.TestCaseResult protoTestCaseResult =
                protoExecutionResponse.getTestCasesResultMap().get("input1");
        assertNotNull(protoTestCaseResult);
        assertEquals("Accepted", protoTestCaseResult.getVerdict());
        assertEquals(100, protoTestCaseResult.getVerdictStatusCode());
        assertEquals("output", protoTestCaseResult.getOutput());
        assertEquals("error", protoTestCaseResult.getError());
        assertEquals("expectedOutput", protoTestCaseResult.getExpectedOutput());
        assertEquals(5, protoTestCaseResult.getExecutionDuration());
    }
}
