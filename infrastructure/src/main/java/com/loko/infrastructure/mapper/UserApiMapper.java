package com.loko.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.loko.applications.dto.user.UserCreationDto;
import com.loko.applications.dto.user.UserDto;
import com.loko.domain.Role;
import com.loko.domain.User;

@Mapper(componentModel = "spring")
public interface UserApiMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "authorities",ignore = true)
    User toUser(UserCreationDto dto);

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "active", target = "isActive")
    UserCreationDto toUserCreationDto(User user);

    @Mapping(target = "role", qualifiedByName = "rolesToRoleNames")
    UserDto toUserDto(User user);

    @Named("rolesToRoleNames")
    default String rolesToRoleNames(Role role) {
        if (role == null) {
            return null;
        }
        return role.getName();
    }
}
