package com.loko.applications.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyEmailRequestDto(
    @NotBlank(message = "Token must not be blank")
    String token,
    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.")
    String password,
    @NotBlank(message = "Confirm Password must not be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Confirm Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.")
    String confirmPassword
) {
    
}
