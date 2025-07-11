package com.wallet.entrypoints.web;

import com.wallet.entrypoints.web.dto.response.ErrorDTO;
import com.wallet.exceptions.NotFoundException;
import com.wallet.exceptions.TransactionIdentificationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    public ErrorDTO handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ErrorDTO.builder()
                .status(CONFLICT.value())
                .error(CONFLICT.getReasonPhrase())
                .message("Transaction with provided correlation ID already exists.")
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDTO handleNotFoundException(NotFoundException e) {
        return ErrorDTO.builder()
                .status(NOT_FOUND.value())
                .error(NOT_FOUND.getReasonPhrase())
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
