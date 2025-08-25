package com.loko.infrastructure.persistence.activity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.loko.domain.ActivityLog;
import com.loko.domain.User;
import com.loko.infrastructure.entities.ActivityLogEntity;
import com.loko.infrastructure.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface ActivityPersistenceMapper {

    @Mapping(source = "user", target = "user", qualifiedByName = "userEntityToDomain")
    ActivityLog toDomain(ActivityLogEntity entity);

    @Named("userEntityToDomain")
    default User userEntityToDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        user.setId(entity.getId().toString());
        return user;
    }

    @Mapping(target = "user.authorities", ignore = true)
    @Mapping(target = "user.role", ignore = true)
    ActivityLogEntity toEntity(ActivityLog domain);
}
