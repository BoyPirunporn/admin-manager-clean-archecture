package com.loko.applications.dto;

import java.time.Instant;

public record ApiBaseResponse(
    String message,
    int status,
    Instant timestamp
) {
    public ApiBaseResponse(String message, int status) {
        this(message, status, Instant.now());
    }
}
