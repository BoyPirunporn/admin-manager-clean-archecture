package com.loko.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loko.applications.dto.role.RoleCreationDto;
import com.loko.applications.dto.role.RoleDetailDto;
import com.loko.applications.dto.role.RoleDto;
import com.loko.domain.Role;

@Mapper(componentModel = "spring", uses = PermissionApiMapper.class)
public interface RoleApiMapper {
    RoleDto toRoleDto(Role role);

    @Mapping(target = "permissions", ignore = true)
    RoleDetailDto toRoleDetailDto(Role role);

    Role toRole(RoleCreationDto dto);
}
