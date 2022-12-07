package com.cp.compiler.mappers;

import com.cp.compiler.models.testcases.ConvertedTestCase;
import com.cp.compiler.models.testcases.TestCase;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Test case mapper.
 */
public abstract class TestCaseMapper {
    
    private TestCaseMapper() {}
    
    /**
     * To converted test case converted test case.
     *
     * @param testCase   the test case
     * @param testCaseId the test case id
     * @return the converted test case
     * @throws IOException the io exception
     */
    public static ConvertedTestCase toConvertedTestCase(TestCase testCase, String testCaseId) throws IOException {
        var convertedTestCase = new ConvertedTestCase();
        convertedTestCase.setTestCaseId(testCaseId);
        convertedTestCase.setInputFile(getInput(testCase.getInput(), testCaseId));
        convertedTestCase.setExpectedOutputFile(getExpectedOutput(testCase.getExpectedOutput(), testCaseId));
        return convertedTestCase;
    }
    
    /**
     * To converted test cases list.
     *
     * @param testCases the test cases
     * @return the list
     * @throws IOException the io exception
     */
    public static List<ConvertedTestCase> toConvertedTestCases(Map<String, TestCase> testCases) throws IOException {
        List<ConvertedTestCase> convertedTestCases = new ArrayList<>();
        for (String id : testCases.keySet()) {
            convertedTestCases.add(toConvertedTestCase(testCases.get(id), id));
        }
        return convertedTestCases;
    }
    
    private static MultipartFile getExpectedOutput(String expectedOutput, String id) throws IOException {
        return new MockMultipartFile(
                id + "-" + WellKnownFiles.EXPECTED_OUTPUT_FILE_NAME,
                id + "-" + WellKnownFiles.EXPECTED_OUTPUT_FILE_NAME,
                null,
                new ByteArrayInputStream(expectedOutput.getBytes()));
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
