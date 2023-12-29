package com.cp.compiler.exceptions.problems;

import com.cp.compiler.exceptions.ErrorCode;
import com.cp.compiler.exceptions.ErrorType;
import com.cp.compiler.exceptions.MonitoredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidProblemException extends MonitoredException {
    
    public InvalidProblemException(String message) {
        super(message, ErrorCode.COMPILER_SERVER_INTERNAL_ERROR, ErrorType.FATAL);
    }
}
