package com.cp.compiler;

import com.cp.compiler.service.ContainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

@SpringBootTest
public class ContainerServiceTests {
	
	@Autowired
	ContainService containService;
	
	@Test
	public void whenReadOutputMethodIsCalledShouldReturnTheCorrectOutput() throws FileNotFoundException {
		// Given
		BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/outputs/Test1.txt"));
		
		// When
		String output = ReflectionTestUtils.invokeMethod(containService, "readOutput", bufferedReader);
	
		// Then
		Assertions.assertEquals("0123456789\n", output);
	}
	
	@Test
	public void whenCompareExpectedOutputAndContainerOutputShouldTrimBothStrings() throws FileNotFoundException {
		// Given
		String expectedOutput = "abcd";
		String containerOutput = " abcd  ";
		// When
		boolean compareResult = ReflectionTestUtils.invokeMethod(containService, "compareResult", expectedOutput, containerOutput);
		
		// Then
		Assertions.assertEquals(true, compareResult);
	}
}
