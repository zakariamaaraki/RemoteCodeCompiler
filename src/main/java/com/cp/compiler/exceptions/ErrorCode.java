package com.cp.compiler.exceptions;

/**
 * This enum contains all monitored exceptions that may be returned to the caller.
 * For each error created here a counter will be created (see ErrorCounterFactory class).
 *
 * @author Zakaria Maaraki
 */
public enum ErrorCode {
    COMPILER_SERVER_INTERNAL_ERROR, // For unexpected errors
    CONTAINER_BUILD_ERROR, // Occurs during container image build
    CONTAINER_FAILED_DEPENDENCY_ERROR, // Occurs when we can't talk with docker daemon
    COMPILATION_TIMEOUT_ERROR, // Occurs when the compilation exceed the timeout
    RESOURCE_LIMIT_REACHED_ERROR, // Occurs when memory used during compilation exceed the threshold
    THROTTLING_ERROR, // Occurs when a request should be throttled
    BAD_REQUEST, // Occurs when a user send a bad request
    NOT_FOUND // Not found
}
