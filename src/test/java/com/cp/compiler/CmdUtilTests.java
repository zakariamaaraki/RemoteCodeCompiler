package com.cp.compiler;

import com.cp.compiler.utility.CmdUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CmdUtilTests {
	
	@Test
	public void shouldExecuteACommandAndReturnOutput() throws IOException {
		// Given
		String[] cmd = new String[] {"echo", "test"};
		
		// When
		String output  = CmdUtil.runCmd(cmd);
		
		// Then
		Assertions.assertEquals("test\n", output);
	}
	
	@Test
	public void whenReadOutputMethodIsCalledShouldReturnTheCorrectOutput() throws IOException {
		// Given
		BufferedReader bufferedReader = new BufferedReader(new FileReader("src/test/resources/outputs/Test1.txt"));
		
		// When
		String output = CmdUtil.readOutput(bufferedReader);
		
		// Then
		Assertions.assertEquals("0123456789\n", output);
	}
}
