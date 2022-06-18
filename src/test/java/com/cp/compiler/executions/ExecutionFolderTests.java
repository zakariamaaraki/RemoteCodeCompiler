package com.cp.compiler.executions;

import com.cp.compiler.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class ExecutionFolderTests {
    
    private static final String DOCKERFILE = "Dockerfile";
    
    @Test
    void javaExecutionFolderShouldExist() {
        // Given
        File javaExecutionFolder = new File(Language.JAVA.getFolderName());
        
        // Then
        Assertions.assertTrue(javaExecutionFolder.exists());
        Assertions.assertTrue(javaExecutionFolder.isDirectory());
    }
    
    @Test
    void javaExecutionFolderShouldContainsADockerfile() {
        // Given
        File javaExecutionFolder = new File(Language.JAVA.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(javaExecutionFolder.exists());
        Assertions.assertTrue(javaExecutionFolder.isFile());
    }
    
    @Test
    void pythonExecutionFolderShouldExist() {
        // Given
        File pythonExecutionFolder = new File(Language.PYTHON.getFolderName());
        
        // Then
        Assertions.assertTrue(pythonExecutionFolder.exists());
        Assertions.assertTrue(pythonExecutionFolder.isDirectory());
    }
    
    @Test
    void pythonExecutionFolderShouldContainsADockerfile() {
        // Given
        File pythonExecutionFolder = new File(Language.PYTHON.getFolderName()+ "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(pythonExecutionFolder.exists());
        Assertions.assertTrue(pythonExecutionFolder.isFile());
    }
    
    @Test
    void kotlinExecutionFolderShouldExist() {
        // Given
        File kotlinExecutionFolder = new File(Language.KOTLIN.getFolderName());
        
        // Then
        Assertions.assertTrue(kotlinExecutionFolder.exists());
        Assertions.assertTrue(kotlinExecutionFolder.isDirectory());
    }
    
    @Test
    void kotlinExecutionFolderShouldContainsADockerfile() {
        // Given
        File kotlinExecutionFolder = new File(Language.KOTLIN.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(kotlinExecutionFolder.exists());
        Assertions.assertTrue(kotlinExecutionFolder.isFile());
    }
    
    @Test
    void cExecutionFolderShouldExist() {
        // Given
        File cExecutionFolder = new File(Language.C.getFolderName());
        
        // Then
        Assertions.assertTrue(cExecutionFolder.exists());
        Assertions.assertTrue(cExecutionFolder.isDirectory());
    }
    
    @Test
    void cExecutionFolderShouldContainsADockerfile() {
        // Given
        File cExecutionFolder = new File(Language.C.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(cExecutionFolder.exists());
        Assertions.assertTrue(cExecutionFolder.isFile());
    }
    
    @Test
    void cppExecutionFolderShouldExist() {
        // Given
        File cppExecutionFolder = new File(Language.CPP.getFolderName());
        
        // Then
        Assertions.assertTrue(cppExecutionFolder.exists());
        Assertions.assertTrue(cppExecutionFolder.isDirectory());
    }
    
    @Test
    void cppExecutionFolderShouldContainsADockerfile() {
        // Given
        File cppExecutionFolder = new File(Language.CPP.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(cppExecutionFolder.exists());
        Assertions.assertTrue(cppExecutionFolder.isFile());
    }
    
    @Test
    void csExecutionFolderShouldExist() {
        // Given
        File csExecutionFolder = new File(Language.CS.getFolderName());
        
        // Then
        Assertions.assertTrue(csExecutionFolder.exists());
        Assertions.assertTrue(csExecutionFolder.isDirectory());
    }
    
    @Test
    void csExecutionFolderShouldContainsADockerfile() {
        // Given
        File csExecutionFolder = new File(Language.CS.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(csExecutionFolder.exists());
        Assertions.assertTrue(csExecutionFolder.isFile());
    }
    
    @Test
    void goExecutionFolderShouldExist() {
        // Given
        File goExecutionFolder = new File(Language.GO.getFolderName());
        
        // Then
        Assertions.assertTrue(goExecutionFolder.exists());
        Assertions.assertTrue(goExecutionFolder.isDirectory());
    }
    
    @Test
    void goExecutionFolderShouldContainsADockerfile() {
        // Given
        File goExecutionFolder = new File(Language.GO.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(goExecutionFolder.exists());
        Assertions.assertTrue(goExecutionFolder.isFile());
    }
}
