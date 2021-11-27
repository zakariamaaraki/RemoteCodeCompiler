package com.cp.compiler.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The type Cmd util tests.
 */
class CmdUtilTests {
	
	/**
	 * Should execute a command and return output.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void shouldExecuteACommandAndReturnOutput() throws IOException {
		// Given
		String[] cmd = new String[] {"echo", "test"};
		
		// When
		String output  = CmdUtil.runCmd(cmd);
		
		// Then
		Assertions.assertEquals("test\n", output);
	}
	
	/**
	 * When read output method is called should return the correct output.
	 *
	 * @throws IOException the io exception
	 */
	@Test
	void whenReadOutputMethodIsCalledShouldReturnTheCorrectOutput() throws IOException {
		// Given
		BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/outputs/Test1.txt"));
		
		// When
		String output = CmdUtil.readOutput(bufferedReader);
		
		// Then
		Assertions.assertEquals("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", output);
	}
}
