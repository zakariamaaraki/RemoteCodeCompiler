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
        File dockerfile = new File(Language.JAVA.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
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
        File dockerfile = new File(Language.PYTHON.getFolderName()+ "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
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
        File dockerfile = new File(Language.KOTLIN.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
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
        File dockerfile = new File(Language.C.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
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
        File dockerfile = new File(Language.CPP.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
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
        File dockerfile = new File(Language.CS.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
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
        File dockerfile = new File(Language.GO.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
    }
    
    @Test
    void scalaExecutionFolderShouldExist() {
        // Given
        File scalaExecutionFolder = new File(Language.SCALA.getFolderName());
        
        // Then
        Assertions.assertTrue(scalaExecutionFolder.exists());
        Assertions.assertTrue(scalaExecutionFolder.isDirectory());
    }
    
    @Test
    void scalaExecutionFolderShouldContainsADockerfile() {
        // Given
        File dockerfile = new File(Language.SCALA.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
    }
    
    @Test
    void rustExecutionFolderShouldExist() {
        // Given
        File rustExecutionFolder = new File(Language.RUST.getFolderName());
        
        // Then
        Assertions.assertTrue(rustExecutionFolder.exists());
        Assertions.assertTrue(rustExecutionFolder.isDirectory());
    }
    
    @Test
    void rustExecutionFolderShouldContainsADockerfile() {
        // Given
        File dockerfile = new File(Language.RUST.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
    }
    
    @Test
    void rubyExecutionFolderShouldExist() {
        // Given
        File rubyExecutionFolder = new File(Language.RUBY.getFolderName());
        
        // Then
        Assertions.assertTrue(rubyExecutionFolder.exists());
        Assertions.assertTrue(rubyExecutionFolder.isDirectory());
    }
    
    @Test
    void rubyExecutionFolderShouldContainsADockerfile() {
        // Given
        File dockerfile = new File(Language.RUBY.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
    }
    
    @Test
    void haskellExecutionFolderShouldExist() {
        // Given
        File haskellExecutionFolder = new File(Language.HASKELL.getFolderName());
        
        // Then
        Assertions.assertTrue(haskellExecutionFolder.exists());
        Assertions.assertTrue(haskellExecutionFolder.isDirectory());
    }
    
    @Test
    void haskellExecutionFolderShouldContainsADockerfile() {
        // Given
        File dockerfile = new File(Language.HASKELL.getFolderName() + "/" + DOCKERFILE);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
    }
}
