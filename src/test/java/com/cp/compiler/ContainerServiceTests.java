package com.cp.compiler;

import com.cp.compiler.service.ContainerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class ContainerServiceTests {
	
	@Autowired
	ContainerService containerService;
	
	@Test
	public void whenCompareExpectedOutputAndContainerOutputShouldTrimBothStrings() {
		// Given
		String expectedOutput = "abcd";
		String containerOutput = " abcd  ";
		
		// When
		boolean compareResult = ReflectionTestUtils.invokeMethod(containerService, "compareResult", expectedOutput, containerOutput);
		
		// Then
		Assertions.assertEquals(true, compareResult);
	}
}
