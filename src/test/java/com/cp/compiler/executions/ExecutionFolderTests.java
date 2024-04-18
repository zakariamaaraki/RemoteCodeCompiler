package com.cp.compiler.executions;

import com.cp.compiler.contract.Language;
import com.cp.compiler.consts.WellKnownFiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class ExecutionFolderTests {
    
    @Test
    void javaExecutionFolderShouldExist() {
        // Given
        File javaExecutionFolder = new File(Language.JAVA.getFolderName());
        
        // Then
        Assertions.assertTrue(javaExecutionFolder.exists());
        Assertions.assertTrue(javaExecutionFolder.isDirectory());
    }
    
    @Test
    void javaExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.JAVA.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
    }
    
    @Test
    void javaExecutionFolderShouldContainsSecurityPolicyFile() {
        // Given
        File securityFile =
                new File(Language.JAVA.getFolderName() + "/" + WellKnownFiles.JAVA_SECURITY_POLICY_FILE_NAME);
        
        // Then
        Assertions.assertTrue(securityFile.exists());
        Assertions.assertTrue(securityFile.isFile());
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
    void pythonExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.PYTHON.getFolderName()+ "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void kotlinExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.KOTLIN.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void cExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.C.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void cppExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.CPP.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void csExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.CS.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void goExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.GO.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void scalaExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.SCALA.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void rustExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.RUST.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void rubyExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.RUBY.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
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
    void haskellExecutionFolderShouldContainsExecutionDockerfile() {
        // Given
        File dockerfile =
                new File(Language.HASKELL.getFolderName() + "/" + WellKnownFiles.EXECUTION_DOCKERFILE_NAME);
        
        // Then
        Assertions.assertTrue(dockerfile.exists());
        Assertions.assertTrue(dockerfile.isFile());
    }
}
