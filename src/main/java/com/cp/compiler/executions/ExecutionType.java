package com.cp.compiler.executions;

import com.cp.compiler.repositories.executions.ExecutionRepository;
import com.cp.compiler.templates.EntrypointFileGenerator;
import io.micrometer.core.instrument.Counter;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The type Execution type.
 * This class contains common parameters between all Executions of the same time (same programming language).
 * It's used to save memory, as it's cached to have fewer pointers in Execution class.
 *
 * @author Zakaria Maaraki
 */
@Getter
@AllArgsConstructor
public class ExecutionType {
    
    // For monitoring purpose it represents the number of executions in parallel for each programming language
    private final Counter executionCounter;

    private final EntrypointFileGenerator entrypointFileGenerator;

    private final ExecutionRepository executionRepository;
}
