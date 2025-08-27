package com.loko.applications.dto.role;

import java.util.Set;

public record PermissionNodeDto(
    String menuId,
    String menuNameEN,
    String menuNameTH,
    String url,
    boolean canView,
    boolean canCreate,
    boolean canUpdate,
    boolean canDelete,
    Set<PermissionNodeDto> children
) {}
