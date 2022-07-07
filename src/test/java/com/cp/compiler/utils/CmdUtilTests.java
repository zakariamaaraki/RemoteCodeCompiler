package com.cp.compiler.utils;

import com.cp.compiler.exceptions.ProcessExecutionException;
import com.cp.compiler.exceptions.ProcessExecutionTimeoutException;
import com.cp.compiler.models.ProcessOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class CmdUtilTests {
    
    /**
     * Should execute a command and return output.
     *
     * @throws IOException the io exception
     */
    
    @Test
    void whenReadOutputMethodIsCalledShouldReturnTheCorrectOutput() throws IOException {
        // Given
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("src/test/resources/outputs/Test1.txt"));
        
        // When
        String output = CmdUtils.readOutput(bufferedReader);
        
        // Then
        Assertions.assertEquals("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", output);
    }
    
    @Test
    void whenCompareExpectedOutputAndContainerOutputShouldTrimBothStrings() {
        // Given
        String expectedOutput = "abcd";
        String containerOutput = " abcd ";
        
        // When
        boolean compareResult = CmdUtils.compareOutput(expectedOutput, containerOutput);
        // Then
        Assertions.assertEquals(true, compareResult);
    }
    
    @Test
    void whenCompareExpectedOutputAndContainerOutputShouldRemoveExtraSpacesInBothStrings() {
        // Given
        String expectedOutput = "abcd c";
        String containerOutput = " abcd  c ";
        
        // When
        boolean compareResult = CmdUtils.compareOutput(expectedOutput, containerOutput);
        
        // Then
        Assertions.assertEquals(true, compareResult);
    }
    
    @Test
    void whenCompareExpectedOutputAndContainerOutputShouldRemoveNewLineCharInBothStrings() {
        // Given
        String expectedOutput = "abcd\nc";
        String containerOutput = " abcd  c\n";
        
        // When
        boolean compareResult = CmdUtils.compareOutput(expectedOutput, containerOutput);
        
        // Then
        Assertions.assertEquals(true, compareResult);
    }
    
    @Test
    void executeProcessShouldTimeoutAnThrowAProcessExecutionTimeoutException() throws ProcessExecutionException{
        // Given
        String[] cmd = new String[] {"sleep", "2000"};
        
        // When
        Assertions.assertThrows(ProcessExecutionTimeoutException.class, () -> CmdUtils.executeProcess(cmd, 1));
    }
    
    @Test
    void executeProcessShouldNotTimeout() throws ProcessExecutionException {
        // Given
        String[] cmd = new String[] {"sleep", "1"};
        
        // When
        ProcessOutput output  = CmdUtils.executeProcess(cmd, 3000);
    }
    
    @Test
    void executeProcessShouldReturnCorrectOutput() throws ProcessExecutionException {
        // Given
        String[] cmd = new String[] {"echo", "test"};
        
        // When
        ProcessOutput output  = CmdUtils.executeProcess(cmd, 3000);
        
        // Then
        Assertions.assertTrue(CmdUtils.compareOutput("test", output.getStdOut()));
    }
    
    @Test
    void executeProcessShouldNotReturnAnError() throws ProcessExecutionException {
        // Given
        String[] cmd = new String[] {"echo", "test"};
        
        // When
        ProcessOutput output  = CmdUtils.executeProcess(cmd, 3000);
        
        // Then
        Assertions.assertEquals("", output.getStdErr());
    }
    
    @Test
    void executeProcessShouldReturnAnError() {
        // Given
        String[] cmd = new String[] {"thisIsNotACmd", "test"};
        
        // When
        Assertions.assertThrows(ProcessExecutionException.class, () -> {
            CmdUtils.executeProcess(cmd, 3000);
        });
    }
    
    @Test
    void shouldThrowIllegalArgumentExceptionIfTimeoutIsLessThan1() {
        // Given
        String[] cmd = new String[] {"thisIsNotACmd", "test"};
        
        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CmdUtils.executeProcess(cmd, 0);
        });
    }
    
    @Test
    void shouldThrowIllegalArgumentExceptionIfCommandIsNull() {
        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CmdUtils.executeProcess(null, 100);
        });
    }
    
    @Test
    void shouldThrowIllegalArgumentExceptionIfCommandIsEmpty() {
        // Given
        String[] cmd = new String[] {};
        
        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CmdUtils.executeProcess(cmd, 100);
        });
    }
}
