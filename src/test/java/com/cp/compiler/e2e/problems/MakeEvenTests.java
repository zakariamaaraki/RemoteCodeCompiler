package com.cp.compiler.e2e.problems;

import com.cp.compiler.controller.CompilerController;
import com.cp.compiler.model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

@SpringBootTest
public class MakeEvenTests {
	
	@Autowired
	private CompilerController compilerController;
	private static final String ACCEPTED_VERDICT = "Accepted";
	private static final String TIME_LIMIT_EXCEEDED_VERDICT = "Time Limit Exceeded";
	private static final String COMPILATION_ERROR_VERDICT = "Compilation Error";
	private static final String WRONG_ANSWER_VERDICT = "Wrong Answer";
	private static final String OUT_OF_MEMORY_VERDICT = "Out Of Memory";
	private static final String RUNTIME_ERROR_VERDICT = "Runtime Error";
	
	@DisplayName("Make even Problem test case 1")
	@Test
	void makeEvenTest1ShouldReturnAcceptedVerdict() throws Exception {
		// Given
		File sourceCodeFile = new File("src/test/resources/sources/problems/MakeEven.py");
		MultipartFile sourceCode = new MockMultipartFile("MakeEven.py", "MakeEven.py", null ,new FileInputStream(sourceCodeFile));
		
		File expectedOutputFile = new File("src/test/resources/outputs/makeEven/makeEven-1.txt");
		MultipartFile expectedOutput = new MockMultipartFile("makeEven-1.txt", "makeEven-1.txt", null, new FileInputStream(expectedOutputFile));
		
		File inputFile = new File("src/test/resources/inputs/makeEven/makeEven-1.txt");
		MultipartFile inputs = new MockMultipartFile("makeEven-1.txt", "makeEven-1.txt", null, new FileInputStream(inputFile));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compilePython(expectedOutput, sourceCode, inputs, 3, 500);
		
		// Then
		Assertions.assertEquals(ACCEPTED_VERDICT, ((Response)responseEntity.getBody()).getStatus());
	}
}
