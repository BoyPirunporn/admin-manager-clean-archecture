package com.loko.infrastructure.persistence.role;

import org.mapstruct.Mapper;

import com.loko.domain.Role;
import com.loko.infrastructure.entities.RoleEntity;

@Mapper(componentModel = "spring",uses = {RolePermissionPersistenceMapper.class})
public interface RolePersistenceMapper {
    Role toDomain(RoleEntity entity);
    RoleEntity toEntity(Role domain);
}
