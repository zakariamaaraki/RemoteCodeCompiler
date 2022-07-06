package com.cp.compiler.models;

import com.cp.compiler.wellknownconstants.WellKnownCommands;
import com.cp.compiler.wellknownconstants.WellKnownFiles;
import com.cp.compiler.wellknownconstants.WellKnownFolders;
import com.cp.compiler.wellknownconstants.WellKnownMetrics;
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
            WellKnownFiles.PYTHON_FILE_NAME,
            WellKnownCommands.PYTHON_COMMAND_LINE,
            WellKnownMetrics.PYTHON_COUNTER_NAME),
    /**
     * C language.
     */
    C(WellKnownFolders.C_EXECUTION_FOLDER_NAME,
            WellKnownFiles.C_FILE_NAME,
            WellKnownCommands.C_COMMAND_LINE,
            WellKnownMetrics.C_COUNTER_NAME),
    /**
     * Cpp language.
     */
    CPP(WellKnownFolders.CPP_EXECUTION_FOLDER_NAME,
            WellKnownFiles.CPP_FILE_NAME,
            WellKnownCommands.CPP_COMMAND_LINE,
            WellKnownMetrics.CPP_COUNTER_NAME),
    /**
     * Java language.
     */
    JAVA(WellKnownFolders.JAVA_EXECUTION_FOLDER_NAME,
            WellKnownFiles.JAVA_FILE_NAME,
            WellKnownCommands.JAVA_COMMAND_LINE,
            WellKnownMetrics.JAVA_COUNTER_NAME),
    /**
     * Golang language.
     */
    GO(WellKnownFolders.GO_EXECUTION_FOLDER_NAME,
            WellKnownFiles.GO_FILE_NAME,
            WellKnownCommands.GO_COMMAND_LINE,
            WellKnownMetrics.GO_COUNTER_NAME),
    /**
     * Cs language.
     */
    CS(WellKnownFolders.CS_EXECUTION_FOLDER_NAME,
            WellKnownFiles.CS_FILE_NAME,
            WellKnownCommands.CS_COMMAND_LINE,
            WellKnownMetrics.CS_COUNTER_NAME),
    /**
     * Kotlin language.
     */
    KOTLIN(WellKnownFolders.KOTLIN_EXECUTION_FOLDER_NAME,
            WellKnownFiles.KOTLIN_FILE_NAME,
            WellKnownCommands.KOTLIN_COMMAND_LINE,
            WellKnownMetrics.KOTLIN_COUNTER_NAME),
    
    /**
     * Scala language.
     */
    SCALA(WellKnownFolders.SCALA_EXECUTION_FOLDER_NAME,
            WellKnownFiles.SCALA_FILE_NAME,
            WellKnownCommands.SCALA_COMMAND_LINE,
            WellKnownMetrics.SCALA_COUNTER_NAME),
    
    /**
     * Rust language.
     */
    RUST(WellKnownFolders.RUST_EXECUTION_FOLDER_NAME,
            WellKnownFiles.RUST_FILE_NAME,
            WellKnownCommands.RUST_COMMAND_LINE,
            WellKnownMetrics.RUST_COUNTER_NAME),
    
    /**
     * Ruby language.
     */
    RUBY(WellKnownFolders.RUBY_EXECUTION_FOLDER_NAME,
            WellKnownFiles.RUBY_FILE_NAME,
            WellKnownCommands.RUBY_COMMAND_LINE,
            WellKnownMetrics.RUBY_COUNTER_NAME),
    
    /**
     * Haskell language.
     */
    HASKELL(WellKnownFolders.HASKELL_EXECUTION_FOLDER_NAME,
            WellKnownFiles.HASKELL_FILE_NAME,
            WellKnownCommands.HASKELL_COMMAND_LINE,
            WellKnownMetrics.HASKELL_COUNTER_NAME);
    
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
    /**
     * The execution counter name
     */
    private String executionCounter;
}