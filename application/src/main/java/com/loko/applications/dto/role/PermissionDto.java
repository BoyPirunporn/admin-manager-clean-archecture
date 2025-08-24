package com.loko.applications.dto.role;

public record PermissionDto(
        String menuId,
        String menuTitle,
        String menuParentId,
        boolean canView,
        boolean canCreate,
        boolean canUpdate,
        boolean canDelete) {
}