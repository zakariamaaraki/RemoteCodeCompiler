package com.cp.compiler.mappers;

import com.cp.compiler.models.testcases.TransformedTestCase;
import com.cp.compiler.contract.testcases.TestCase;
import com.cp.compiler.consts.WellKnownFiles;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Test case mapper.
 *
 * @author Zakaria Maaraki
 */
public abstract class TestCaseMapper {
    
    private TestCaseMapper() {}
    
    /**
     * Converts a testCase into an instance of ConvertedTestCase
     *
     * @param testCase   the test case
     * @param testCaseId the test case id
     * @return the converted test case
     * @throws IOException the io exception
     */
    public static TransformedTestCase toConvertedTestCase(TestCase testCase, String testCaseId) throws IOException {
        var convertedTestCase = new TransformedTestCase();
        convertedTestCase.setTestCaseId(testCaseId);
        convertedTestCase.setInputFile(getInput(testCase.getInput(), testCaseId));
        convertedTestCase.setExpectedOutput(testCase.getExpectedOutput());
        return convertedTestCase;
    }
    
    /**
     * Converts a dictionary of TestCases into a list of ConvertedTestCases
     *
     * @param testCases the test cases
     * @return the list
     * @throws IOException the io exception
     */
    public static List<TransformedTestCase> toConvertedTestCases(Map<String, TestCase> testCases) throws IOException {
        List<TransformedTestCase> convertedTestCases = new ArrayList<>();
        for (String id : testCases.keySet()) {
            convertedTestCases.add(toConvertedTestCase(testCases.get(id), id));
        }
        return convertedTestCases;
    }
    
    private static MultipartFile getInput(String input, String id) throws IOException {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return new MockMultipartFile(
                id + "-" + WellKnownFiles.INPUT_FILE_NAME,
                id + "-" + WellKnownFiles.INPUT_FILE_NAME,
                null,
                new ByteArrayInputStream(input.getBytes()));
    }
}
