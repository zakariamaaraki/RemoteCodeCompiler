package com.cp.compiler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigChecksTests {
    
    @Test
    void configFileShouldBeAValidYamlFile() throws IOException {
        final String path = "src/main/resources";
        File resourcesFolder = new File(path);

        // Resources folder should exist
        Assertions.assertNotNull(resourcesFolder);
        
        List<String> configFiles = Arrays
                .stream(resourcesFolder.listFiles())
                .filter(file -> !file.isDirectory())
                .map(file -> file.getPath())
                .filter(file -> file.endsWith(".yaml") || file.endsWith(".yml"))
                .collect(Collectors.toList());
        
        // At least one config file should exist
        Assertions.assertTrue(configFiles.size() >= 1);
        
        // Each config file should be valid
        for (String configFilePath : configFiles) {
            isValid(configFilePath);
        }
    }
    
    // Will throw an exception if the yaml file is not valid
    private void isValid(String configFilePath) throws IOException {
        File configFile = new File(configFilePath);
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.readValue(configFile, Object.class);
    }
    
}
