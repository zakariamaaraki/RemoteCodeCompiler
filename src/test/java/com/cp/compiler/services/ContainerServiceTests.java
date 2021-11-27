package com.cp.compiler.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * The type Container service tests.
 */
@SpringBootTest
class ContainerServiceTests {
	
	@Autowired
	private ContainerService containerService;
	
	/**
	 * When compare expected output and container output should trim both strings.
	 */
	@Test
	void whenCompareExpectedOutputAndContainerOutputShouldTrimBothStrings() {
		// Given
		String expectedOutput = "abcd";
		String containerOutput = " abcd  ";
		
		// When
		boolean compareResult = ReflectionTestUtils.invokeMethod(containerService, "compareResult", expectedOutput, containerOutput);
		
		// Then
		Assertions.assertEquals(true, compareResult);
	}
}
