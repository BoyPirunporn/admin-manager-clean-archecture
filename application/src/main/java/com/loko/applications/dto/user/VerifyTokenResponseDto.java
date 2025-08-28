package com.loko.applications.dto.user;

public record VerifyTokenResponseDto(
    boolean isValid,
    String state
) {
    
}
