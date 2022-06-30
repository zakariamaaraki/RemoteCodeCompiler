package com.cp.compiler.utilities;

import com.cp.compiler.exceptions.ProcessExecutionException;
import com.cp.compiler.models.ProcessOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
        String output = CmdUtil.readOutput(bufferedReader);
        
        // Then
        Assertions.assertEquals("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", output);
    }
    
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
    
    @Test
    void executeProcessShouldTimeout() throws ProcessExecutionException{
        // Given
        String[] cmd = new String[] {"sleep", "2000"};
        
        // When
        ProcessOutput output  = CmdUtil.executeProcess(cmd, 0, StatusUtil.TIME_LIMIT_EXCEEDED_STATUS);
        
        // Then
        Assertions.assertEquals(StatusUtil.TIME_LIMIT_EXCEEDED_STATUS, output.getStatus());
    }
    
    @Test
    void executeProcessShouldNotTimeout() throws ProcessExecutionException {
        // Given
        String[] cmd = new String[] {"sleep", "1"};
        
        // When
        ProcessOutput output  = CmdUtil.executeProcess(cmd, 3000, StatusUtil.TIME_LIMIT_EXCEEDED_STATUS);
        
        // Then
        Assertions.assertNotEquals(StatusUtil.TIME_LIMIT_EXCEEDED_STATUS, output.getStatus());
    }
    
    @Test
    void executeProcessShouldReturnCorrectOutput() throws ProcessExecutionException {
        // Given
        String[] cmd = new String[] {"echo", "test"};
        
        // When
        ProcessOutput output  = CmdUtil.executeProcess(cmd, 3000, StatusUtil.TIME_LIMIT_EXCEEDED_STATUS);
        
        // Then
        Assertions.assertTrue(CmdUtil.compareOutput("test", output.getStdOut()));
    }
    
    @Test
    void executeProcessShouldNotReturnAnError() throws ProcessExecutionException {
        // Given
        String[] cmd = new String[] {"echo", "test"};
        
        // When
        ProcessOutput output  = CmdUtil.executeProcess(cmd, 3000, StatusUtil.TIME_LIMIT_EXCEEDED_STATUS);
        
        // Then
        Assertions.assertEquals("", output.getStdErr());
    }
    
    @Test
    void executeProcessShouldReturnAnError() {
        // Given
        String[] cmd = new String[] {"thisIsNotACmd", "test"};
        
        // When
        Assertions.assertThrows(ProcessExecutionException.class, () -> {
            CmdUtil.executeProcess(cmd, 3000, StatusUtil.TIME_LIMIT_EXCEEDED_STATUS);
        });
    }
}
