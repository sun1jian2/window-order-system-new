package com.window.system.exception;

import com.window.system.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("System Error", e);
        String message = e.getMessage();
        if (message == null) {
            message = e.getClass().getName();
            if (e.getStackTrace() != null && e.getStackTrace().length > 0) {
                message += " at " + e.getStackTrace()[0].toString();
            }
        }
        return Result.error("System Error: " + message);
    }

    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(BindException e) {
        log.warn("Parameter Error: {}", e.getMessage());
        return Result.error("Parameter Error: " + e.getBindingResult().getFieldError().getDefaultMessage());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Business Error: {}", e.getMessage());
        return Result.error(e.getMessage());
    }
}
