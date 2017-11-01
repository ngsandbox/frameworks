package org.ngsanbox.rest.controllers;

import org.ngsanbox.rest.exceptions.FileProcessError;
import org.ngsanbox.rest.exceptions.RequestNotFound;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RequestNotFound.class})
    protected ResponseEntity<Object> handleDataNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {FileProcessError.class})
    protected ResponseEntity<Object> handleFileProcessError(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Cannot get file content of the request",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}