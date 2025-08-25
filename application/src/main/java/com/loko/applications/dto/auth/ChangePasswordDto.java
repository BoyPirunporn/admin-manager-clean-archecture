package com.loko.applications.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
    @NotBlank(message = "Current Password can not be blank.")
    @Size(min = 8, max = 50, message = "Current Password must be between 2 and 50 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "Current Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.")
    String currentPassword,
    @NotBlank(message = "New Password can not be blank.")
    @Size(min = 8, max = 50, message = "New Password must be between 2 and 50 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "New Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.")
    String newPassword
) {
    
}
