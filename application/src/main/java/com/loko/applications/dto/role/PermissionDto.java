package com.loko.applications.dto.role;

public record PermissionDto(
        String menuId,
        String menuNameEN,
        String menuNameTH,
        String menuParentId,
        boolean canView,
        boolean canCreate,
        boolean canUpdate,
        boolean canDelete) {
}