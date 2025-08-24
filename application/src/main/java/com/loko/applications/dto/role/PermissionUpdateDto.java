package com.loko.applications.dto.role;

import jakarta.validation.constraints.NotNull;

public record PermissionUpdateDto(
    @NotNull(message = "Menu ID cannot be null")
    String menuId,
    boolean canView,
    boolean canCreate,
    boolean canUpdate,
    boolean canDelete
) {}
