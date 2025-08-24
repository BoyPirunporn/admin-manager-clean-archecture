package com.loko.applications.dto.menu;

import java.util.Set;

public record MenuPermissionNodeDto(
    String menuId,
    String menuName,
    String url,
    String icon,
    int menuDisplayOrder,
    boolean isGroup,
    boolean isVisible,
    boolean canView,
    boolean canCreate,
    boolean canUpdate,
    boolean canDelete,
    Set<MenuPermissionNodeDto> children
) {}
