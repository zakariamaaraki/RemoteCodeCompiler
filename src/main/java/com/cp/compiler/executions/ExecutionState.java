package com.cp.compiler.executions;

public enum ExecutionState {
    NotStarted,
    CreatingExecutionEnvironment,
    ExecutionEnvironmentReady,
    Compiling,
    BinariesReady,
    CreatingExecutionContainer,
    ReadyForExecution,
    Running,
    Finished,
    Error
}
