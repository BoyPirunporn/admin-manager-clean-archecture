package com.loko.applications.dto.role;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RoleCreationDto(
    @NotBlank(message = "Role name cannot be blank")
    @Size(min = 2, max = 50, message = "Role name must be between 2 and 50 characters")
    String name,
    @Size(max = 200,message = "Role description max input 200 char")
    String description,

    @NotEmpty(message = "Permissions cannot be empty")
    Set<PermissionUpdateDto> permissions
) {}