package com.cp.compiler.exceptions.problems;

import com.cp.compiler.exceptions.ErrorCode;
import com.cp.compiler.exceptions.ErrorType;
import com.cp.compiler.exceptions.MonitoredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProblemNotFoundException extends MonitoredException {
    
    public ProblemNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND, ErrorType.WARNING);
    }
}
