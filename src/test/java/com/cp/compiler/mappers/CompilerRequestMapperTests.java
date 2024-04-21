package com.cp.compiler.mappers;

import com.cp.compiler.contract.CompilerProto;
import com.cp.compiler.contract.Language;
import com.cp.compiler.contract.RemoteCodeCompilerRequest;
import com.cp.compiler.contract.testcases.TestCase;
import com.cp.compiler.exceptions.CompilerBadRequestException;
import com.cp.compiler.mappers.grpc.CompilerRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class CompilerRequestMapperTests {

    private CompilerRequestMapper mapper;

    @BeforeEach
    public void setup() {
        mapper = Mappers.getMapper(CompilerRequestMapper.class);
    }

    @Test
    public void testToCompilerRequest() {
        // Create a mock proto request
        CompilerProto.RemoteCodeCompilerRequest protoRequest = CompilerProto.RemoteCodeCompilerRequest.newBuilder()
                .setSourcecode("sourceCode")
                .setLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.JAVA)
                .putTestCases("input1", CompilerProto.RemoteCodeCompilerRequest.TestCase.newBuilder()
                        .setInput("testInput")
                        .setExpectedOutput("expectedOutput")
                        .build())
                .build();

        // Call the mapper method
        RemoteCodeCompilerRequest mappedRequest = mapper.toCompilerRequest(protoRequest);

        // Assertions
        assertNotNull(mappedRequest);
        assertEquals("sourceCode", mappedRequest.getSourcecode());
        assertEquals(Language.JAVA, mappedRequest.getLanguage());
        assertEquals(1, mappedRequest.getTestCases().size());
        assertTrue(mappedRequest.getTestCases().containsKey("input1"));
        TestCase testCase = mappedRequest.getTestCases().get("input1");
        assertEquals("testInput", testCase.getInput());
        assertEquals("expectedOutput", testCase.getExpectedOutput());
    }

    @Test
    public void testToLanguage() {
        // Test each supported language mapping
        assertEquals(Language.C, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.C));
        assertEquals(Language.CPP, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.CPP));
        assertEquals(Language.CS, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.CS));
        assertEquals(Language.JAVA, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.JAVA));
        assertEquals(Language.KOTLIN, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.KOTLIN));
        assertEquals(Language.SCALA, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.SCALA));
        assertEquals(Language.GO, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.GO));
        assertEquals(Language.HASKELL, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.HASKELL));
        assertEquals(Language.PYTHON, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.PYTHON));
        assertEquals(Language.RUBY, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.RUBY));
        assertEquals(Language.RUST, CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.RUST));

        // Test for unrecognized language
        assertThrows(CompilerBadRequestException.class, () -> {
            CompilerRequestMapper.toLanguage(CompilerProto.RemoteCodeCompilerRequest.Language.UNRECOGNIZED);
        });
    }

    @Test
    public void testToTestCases() {
        // Create a mock map of test cases
        Map<String, CompilerProto.RemoteCodeCompilerRequest.TestCase> protoTestCases = new LinkedHashMap<>();
        protoTestCases.put("input1", CompilerProto.RemoteCodeCompilerRequest.TestCase.newBuilder()
                .setInput("testInput1")
                .setExpectedOutput("expectedOutput1")
                .build());
        protoTestCases.put("input2", CompilerProto.RemoteCodeCompilerRequest.TestCase.newBuilder()
                .setInput("testInput2")
                .setExpectedOutput("expectedOutput2")
                .build());

        // Call the mapper method
        LinkedHashMap<String, TestCase> mappedTestCases = CompilerRequestMapper.toTestCases(protoTestCases);

        // Assertions
        assertNotNull(mappedTestCases);
        assertEquals(2, mappedTestCases.size());
        assertTrue(mappedTestCases.containsKey("input1"));
        assertTrue(mappedTestCases.containsKey("input2"));
        TestCase testCase1 = mappedTestCases.get("input1");
        assertEquals("testInput1", testCase1.getInput());
        assertEquals("expectedOutput1", testCase1.getExpectedOutput());
        TestCase testCase2 = mappedTestCases.get("input2");
        assertEquals("testInput2", testCase2.getInput());
        assertEquals("expectedOutput2", testCase2.getExpectedOutput());
    }
}