package com.loko.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.loko.applications.dto.activity.ActivityLogDto;
import com.loko.applications.dto.activity.ActivityLogRequestDto;
import com.loko.domain.ActivityLog;

@Mapper(componentModel = "spring")
public interface ActivityApiMapper {

    ActivityLogDto toDto(ActivityLog domain);

    @Mapping(target = "user", ignore = true)
    ActivityLog toDomain(ActivityLogRequestDto dto);
}
