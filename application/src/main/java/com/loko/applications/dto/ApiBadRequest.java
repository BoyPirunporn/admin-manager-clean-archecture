package com.loko.applications.dto;

public record ApiBadRequest<T>(
    String message,
    int status,
    T errors
) {
    
}
