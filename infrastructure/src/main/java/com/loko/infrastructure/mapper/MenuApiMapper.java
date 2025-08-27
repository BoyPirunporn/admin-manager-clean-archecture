package com.loko.infrastructure.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loko.applications.dto.menu.MenuDto;
import com.loko.applications.dto.menu.MenuPermissionNodeDto;
import com.loko.domain.Menu;
import com.loko.domain.RolePermission;

@Mapper(componentModel = "spring")
public interface MenuApiMapper {
    @Mapping(source = "parent.id", target = "parentId")
    MenuDto toDto(Menu menu);

    @Mapping(source = "permission.menu.id", target = "menuId")
    @Mapping(source = "permission.menu.nameEN", target = "nameEN")
    @Mapping(source = "permission.menu.nameTH", target = "nameTH")
    @Mapping(source = "permission.menu.url", target = "url")
    @Mapping(source = "permission.menu.icon", target = "icon")
    @Mapping(source = "permission.menu.group", target = "isGroup")
    @Mapping(source = "permission.menu.displayOrder", target = "menuDisplayOrder")
    @Mapping(source = "permission.menu.visible", target = "isVisible")
    @Mapping(source = "permission.canView", target = "canView")
    @Mapping(source = "permission.canCreate", target = "canCreate")
    @Mapping(source = "permission.canUpdate", target = "canUpdate")
    @Mapping(source = "permission.canDelete", target = "canDelete")
    @Mapping(source = "children", target = "children")
    MenuPermissionNodeDto toMenuPermissionNodeDto(RolePermission permission, Set<MenuPermissionNodeDto> children);
}
