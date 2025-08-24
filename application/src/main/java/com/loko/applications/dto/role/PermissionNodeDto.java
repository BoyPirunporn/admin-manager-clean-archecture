package com.loko.applications.dto.role;

import java.util.Set;

public record PermissionNodeDto(
    String menuId,
    String menuName,
    String url,
    boolean canView,
    boolean canCreate,
    boolean canUpdate,
    boolean canDelete,
    Set<PermissionNodeDto> children
) {}
