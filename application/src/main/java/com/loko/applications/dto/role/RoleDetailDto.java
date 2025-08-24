package com.loko.applications.dto.role;

import java.util.Set;

public record RoleDetailDto(
        String id,
        String name,
        String description,
        Set<PermissionNodeDto> permissions) {

}
