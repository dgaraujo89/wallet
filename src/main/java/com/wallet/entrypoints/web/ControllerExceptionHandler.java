package com.wallet.entrypoints.web;

import com.wallet.entrypoints.web.dto.response.ErrorDTO;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleValidationException(MethodArgumentNotValidException e) {
        final var errorBuilder = ErrorDTO.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message("Validation failed");

        e.getAllErrors()
                .forEach(error -> errorBuilder.addField(((FieldError) error).getField(), error.getDefaultMessage()));

        return errorBuilder.build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException e) {
        return ErrorDTO.builder()
                .status(BAD_REQUEST.value())
                .error(BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception e) {
        e.printStackTrace();
        return ErrorDTO.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .error(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Internal Server Error")
                .build();
    }

}
