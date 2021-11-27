package com.cp.compiler.e2e.problems;

import com.cp.compiler.controllers.CompilerController;
import com.cp.compiler.models.Response;
import com.cp.compiler.models.Verdict;
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

// problem from Codeforces

@SpringBootTest
public class PhysEdOnlineTests {
	
	@Autowired
	private CompilerController compilerController;
	
	@DisplayName("Phys Ed Online Problem test case 1")
	@Test
	void physEdOnlineTest1ShouldReturnAcceptedVerdict() throws Exception {
		// Given
		File sourceCodeFile = new File("src/test/resources/sources/problems/PhysEdOnline.cpp");
		MultipartFile sourceCode = new MockMultipartFile("PhysEdOnline.cpp", "PhysEdOnline.cpp", null ,new FileInputStream(sourceCodeFile));
		
		File expectedOutputFile = new File("src/test/resources/outputs/physEdOnline/physEdOnline-1.txt");
		MultipartFile expectedOutput = new MockMultipartFile("physEdOnline-1.txt", "physEdOnline-1.txt", null, new FileInputStream(expectedOutputFile));
		
		File inputFile = new File("src/test/resources/inputs/physEdOnline/physEdOnline-1.txt");
		MultipartFile inputs = new MockMultipartFile("physEdOnline-1.txt", "physEdOnline-1.txt", null, new FileInputStream(inputFile));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compileCpp(expectedOutput, sourceCode, inputs, 3, 500);
		
		// Then
		Assertions.assertEquals(Verdict.ACCEPTED.getValue(), ((Response)responseEntity.getBody()).getStatus());
	}
	
	@DisplayName("Phys Ed Online Problem test case 2")
	@Test
	void physEdOnlineTest2ShouldReturnAcceptedVerdict() throws Exception {
		// Given
		File sourceCodeFile = new File("src/test/resources/sources/problems/PhysEdOnline.cpp");
		MultipartFile sourceCode = new MockMultipartFile("physEdOnline.cpp", "physEdOnline.cpp", null ,new FileInputStream(sourceCodeFile));
		
		File expectedOutputFile = new File("src/test/resources/outputs/physEdOnline/physEdOnline-2.txt");
		MultipartFile expectedOutput = new MockMultipartFile("physEdOnline-2.txt", "physEdOnline-2.txt", null, new FileInputStream(expectedOutputFile));
		
		File inputFile = new File("src/test/resources/inputs/physEdOnline/physEdOnline-2.txt");
		MultipartFile inputs = new MockMultipartFile("physEdOnline-2.txt", "physEdOnline-2.txt", null, new FileInputStream(inputFile));
		
		// When
		ResponseEntity<Object> responseEntity = compilerController.compileCpp(expectedOutput, sourceCode, inputs, 3, 500);
		
		// Then
		Assertions.assertEquals(Verdict.ACCEPTED.getValue(), ((Response)responseEntity.getBody()).getStatus());
	}


}
