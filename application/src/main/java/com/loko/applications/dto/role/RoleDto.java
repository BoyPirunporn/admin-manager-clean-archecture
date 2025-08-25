package com.loko.applications.dto.role;

import java.time.LocalDateTime;

public record RoleDto(
    String id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
} 