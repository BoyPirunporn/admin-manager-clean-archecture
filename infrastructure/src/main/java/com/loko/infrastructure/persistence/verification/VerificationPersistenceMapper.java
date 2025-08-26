package com.loko.infrastructure.persistence.verification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loko.domain.VerificationToken;
import com.loko.infrastructure.entities.VerificationTokenEntity;

@Mapper(componentModel = "spring")
public interface VerificationPersistenceMapper {
    @Mapping(target = "user.role.permissions", ignore = true)
    @Mapping(target = "user.authorities", ignore = true)
    VerificationToken toDomain(VerificationTokenEntity entity);

    @Mapping(target = "user.role.permissions", ignore = true)
    @Mapping(target = "user.authorities", ignore = true)
    VerificationTokenEntity toEntity(VerificationToken dto);
}
