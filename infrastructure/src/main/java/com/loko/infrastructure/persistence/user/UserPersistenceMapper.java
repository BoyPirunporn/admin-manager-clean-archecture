package com.loko.infrastructure.persistence.user;

import java.util.Collection;
import java.util.HashSet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.loko.domain.Role;
import com.loko.domain.User;
import com.loko.infrastructure.entities.RoleEntity;
import com.loko.infrastructure.entities.UserEntity;
import com.loko.infrastructure.persistence.role.RolePersistenceMapper;

@Mapper(componentModel = "spring", uses = { RolePersistenceMapper.class })
public interface UserPersistenceMapper {

    @Mapping(source = "role", target = "authorities", qualifiedByName = "toDomainAuthorities")
    User toDomain(UserEntity entity);

    @Mapping(source = "role", target = "authorities", qualifiedByName = "toAuthorities")
    UserEntity toEntity(User domain);

    @Named("toDomainAuthorities")
    default Collection<Object> toDomainAuthorities(RoleEntity role) {
        Collection<Object> authorities = new HashSet<>();

        if (role == null) {
            return authorities;
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().replace(" ", "_").toUpperCase()));
        return authorities;
    }

    @Named("toAuthorities")
    default Collection<GrantedAuthority> toAuthorities(Role role) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        if (role == null) {
            return authorities;
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().replace(" ", "_").toUpperCase()));
        return authorities;
    }
}
