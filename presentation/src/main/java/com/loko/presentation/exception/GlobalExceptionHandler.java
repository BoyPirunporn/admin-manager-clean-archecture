package com.loko.presentation.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.loko.applications.dto.ApiBadRequest;
import com.loko.applications.dto.ApiBaseResponse;
import com.loko.domain.exception.BadRequestException;
import com.loko.domain.exception.DuplicateResourceException;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.domain.exception.UnauthorizeException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings(value = "null")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        System.out.println("EXCEPTION -> " + ex.getMessage());
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            
            errors.computeIfAbsent(fieldError.getField(), k -> new ArrayList<>())
                    .add(fieldError.getDefaultMessage());
        });

        ApiBadRequest<Object> response = new ApiBadRequest<>("Validation failed", HttpStatus.BAD_REQUEST.value(),
                errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiBaseResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiBaseResponse response = new ApiBaseResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiBaseResponse> handleBadRequest(BadRequestException ex) {
        ApiBaseResponse response = new ApiBaseResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiBaseResponse> handleDuplicateResource(DuplicateResourceException ex) {
        ApiBaseResponse response = new ApiBaseResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // A general handler for other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiBaseResponse> handleGenericException(Exception ex) {
        ex.printStackTrace();
        // Log the exception here for debugging purposes
        // logger.error("An unexpected error occurred", ex);
        ApiBaseResponse response = new ApiBaseResponse("An internal server error occurred.",
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings(value = "null")
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ApiBaseResponse response = new ApiBaseResponse("Invalid JSON request body ", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizeException.class)
    protected ResponseEntity<ApiBaseResponse> unauthorize(UnauthorizeException ex) {
        ApiBaseResponse response = new ApiBaseResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
