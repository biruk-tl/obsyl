package com.obsyl.ingestion.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INVALID_LOG_REQUEST_CODE = "INVALID_LOG_REQUEST";

    @ExceptionHandler(InvalidLogRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidLogRequest(InvalidLogRequestException exception) {
        return new ErrorResponse(
                INVALID_LOG_REQUEST_CODE,
                exception.getMessage(),
                Instant.now()
        );
    }
}
