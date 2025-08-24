package com.loko.infrastructure.persistence.role;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loko.domain.RolePermission;
import com.loko.infrastructure.entities.RolePermissionEntity;
import com.loko.infrastructure.persistence.menu.MenuPersistenceMapper;

@Mapper(componentModel = "spring",uses = {MenuPersistenceMapper.class})
public interface RolePermissionPersistenceMapper {
    RolePermission toDomain(RolePermissionEntity entity);

    @Mapping(target = "role", ignore = true)
    RolePermissionEntity toEntity(RolePermission domain);
}
