package com.loko.infrastructure.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.activity.ActivityLogDto;
import com.loko.applications.dto.activity.ActivityLogRequestDto;
import com.loko.applications.ports.in.activity.ActivityUseCase;
import com.loko.applications.ports.out.activity.ActivityRepositoryPort;
import com.loko.domain.ActivityLog;
import com.loko.infrastructure.mapper.ActivityApiMapper;

@Service
public class ActivityService implements ActivityUseCase {
    private final ActivityRepositoryPort repositoryPort;
    private final ActivityApiMapper mapper;

    

    public ActivityService(ActivityRepositoryPort repositoryPort, ActivityApiMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.mapper = mapper;
    }



    @Override
    public PagedResult<ActivityLogDto> dataTable(PageQuery query) {
        PagedResult<ActivityLog> pageResult = repositoryPort.findPageable(query);

        List<ActivityLogDto> toDtos = pageResult.data().stream().map(mapper::toDto).toList();

        return new PagedResult<>(
                toDtos,
                pageResult.totalElements(),
                pageResult.totalPages(),
                pageResult.pageNumber(),
                pageResult.pageSize(),
                pageResult.isLast());
    }



    @Override
    public ActivityLogDto create(ActivityLogRequestDto dto) {
       ActivityLog domain = mapper.toDomain(dto);
       return mapper.toDto(repositoryPort.save(domain));
    }

}
