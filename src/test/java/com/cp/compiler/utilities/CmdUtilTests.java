package com.cp.compiler.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("src/test/resources/outputs/Test1.txt"));
        
        // When
        String output = CmdUtil.readOutput(bufferedReader);
        
        // Then
        Assertions.assertEquals("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", output);
    }
    
    /**
     * When compare expected output and container output should trim both strings.
     */
    @Test
    void whenCompareExpectedOutputAndContainerOutputShouldTrimBothStrings() {
        // Given
        String expectedOutput = "abcd";
        String containerOutput = " abcd ";
        
        // When
        boolean compareResult = CmdUtil.compareOutput(expectedOutput, containerOutput);
        // Then
        Assertions.assertEquals(true, compareResult);
    }
    
    /**
     * When compare expected output and container output should remove extra spaces.
     */
    @Test
    void whenCompareExpectedOutputAndContainerOutputShouldRemoveExtraSpacesInBothStrings() {
        // Given
        String expectedOutput = "abcd c";
        String containerOutput = " abcd  c ";
        
        // When
        boolean compareResult = CmdUtil.compareOutput(expectedOutput, containerOutput);
        
        // Then
        Assertions.assertEquals(true, compareResult);
    }
    
    /**
     * When compare expected output and container output should remove newline char.
     */
    @Test
    void whenCompareExpectedOutputAndContainerOutputShouldRemoveNewLineCharInBothStrings() {
        // Given
        String expectedOutput = "abcd\nc";
        String containerOutput = " abcd  c\n";
        
        // When
        boolean compareResult = CmdUtil.compareOutput(expectedOutput, containerOutput);
        
        // Then
        Assertions.assertEquals(true, compareResult);
    }
}
