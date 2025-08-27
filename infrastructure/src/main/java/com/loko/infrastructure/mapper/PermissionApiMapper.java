package com.loko.infrastructure.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loko.applications.dto.role.PermissionDto;
import com.loko.applications.dto.role.PermissionNodeDto;
import com.loko.applications.dto.role.PermissionUpdateDto;
import com.loko.domain.RolePermission;

@Mapper(componentModel = "spring",uses = {MenuApiMapper.class})
public interface PermissionApiMapper{
    @Mapping(source = "menu.id", target = "menuId")
    @Mapping(source = "menu.nameEN", target = "menuNameEN")
    @Mapping(source = "menu.nameTH", target = "menuNameTH")
    @Mapping(source = "menu.parent.id",target = "menuParentId")
    PermissionDto toPermissionDto(RolePermission permission);

    @Mapping(source = "permission.menu.id", target = "menuId")
    @Mapping(source = "permission.menu.nameEN", target = "menuNameEN")
    @Mapping(source = "permission.menu.nameTH", target = "menuNameTH")
    @Mapping(source = "permission.menu.url", target = "url")
    @Mapping(source = "permission.canView", target = "canView")
    @Mapping(source = "permission.canCreate", target = "canCreate")
    @Mapping(source = "permission.canUpdate", target = "canUpdate")
    @Mapping(source = "permission.canDelete", target = "canDelete")
    @Mapping(source = "children", target = "children")
    PermissionNodeDto toPermissionNodeDto(RolePermission permission, Set<PermissionNodeDto> children);

    @Mapping(source = "menuId", target = "menu.id")
    @Mapping(target = "id",ignore = true)
    RolePermission toRolePermission(PermissionUpdateDto dto);
}
