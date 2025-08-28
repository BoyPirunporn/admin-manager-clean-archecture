package com.loko.applications.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequestDto(
        @NotBlank(message = "Email can not be blank.") 
        @Size(min = 2, max = 50, message = "Email must be between 2 and 50 characters") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email format is invalid.") 
        String email,

        @NotBlank(message = "Password can not be blank.")
        @Size(min = 8, max = 50, message = "Password must be between 2 and 50 characters")
        String password) {

}
