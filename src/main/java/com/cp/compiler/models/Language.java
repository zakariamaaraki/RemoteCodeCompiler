package com.cp.compiler.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The enum Language.
 *
 * @author Zakaria Maaraki
 */
@Getter
@AllArgsConstructor
public enum Language {
    
    /**
     * Python language.
     */
    PYTHON(WellKnownFolders.PYTHON_EXECUTION_FOLDER_NAME,
            WellKnownFileNames.PYTHON_FILE_NAME,
            WellKnownCommands.PYTHON_COMMAND_LINE),
    /**
     * C language.
     */
    C(WellKnownFolders.C_EXECUTION_FOLDER_NAME,
            WellKnownFileNames.C_FILE_NAME,
            WellKnownCommands.C_COMMAND_LINE),
    /**
     * Cpp language.
     */
    CPP(WellKnownFolders.CPP_EXECUTION_FOLDER_NAME,
            WellKnownFileNames.CPP_FILE_NAME,
            WellKnownCommands.CPP_COMMAND_LINE),
    /**
     * Java language.
     */
    JAVA(WellKnownFolders.JAVA_EXECUTION_FOLDER_NAME,
            WellKnownFileNames.JAVA_FILE_NAME,
            WellKnownCommands.JAVA_COMMAND_LINE),
    /**
     * Golang language.
     */
    GO(WellKnownFolders.GO_EXECUTION_FOLDER_NAME,
            WellKnownFileNames.GO_FILE_NAME,
            WellKnownCommands.GO_COMMAND_LINE),
    /**
     * Cs language.
     */
    CS(WellKnownFolders.CS_EXECUTION_FOLDER_NAME,
            WellKnownFileNames.CS_FILE_NAME,
            WellKnownCommands.CS_COMMAND_LINE),
    /**
     * Kotlin language.
     */
    KOTLIN(WellKnownFolders.KOTLIN_EXECUTION_FOLDER_NAME,
            WellKnownFileNames.KOTLIN_FILE_NAME,
            WellKnownCommands.KOTLIN_COMMAND_LINE),
    
    SCALA(WellKnownFolders.SCALA_EXECUTION_FOLDER_NAME,
           WellKnownFileNames.SCALA_FILE_NAME,
           WellKnownCommands.KOTLIN_COMMAND_LINE);
    
    /**
     * The execution folder name.
     */
    private String folderName;
    /**
     * The source code file name.
     */
    private String sourceCodeFileName;
    /**
     * The compilation command.
     */
    private String compilationCommand;
}