package com.loko.infrastructure.persistence.activity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.ports.out.activity.ActivityRepositoryPort;
import com.loko.domain.ActivityLog;
import com.loko.infrastructure.entities.ActivityLogEntity;
import com.loko.infrastructure.entities.UserEntity;
import com.loko.infrastructure.helper.PageableHelper;
import com.loko.infrastructure.repositories.ActivityJpaRepository;
import com.loko.infrastructure.security.SecurityUtils;

@Component
public class ActivityPersistenceAdapter implements ActivityRepositoryPort {
    private final ActivityJpaRepository jpaRepository;
    private final ActivityPersistenceMapper mapper;

    public ActivityPersistenceAdapter(ActivityJpaRepository jpaRepository, ActivityPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PagedResult<ActivityLog> findPageable(PageQuery query) {
        Pageable pageable = PageableHelper.toPageable(query);

        Page<ActivityLogEntity> pageResult = jpaRepository.findAll(pageable);
        // if (query.searchTerm() != null && !query.searchTerm().isBlank()) {
        // pageResult = jpaRepository.findAll(
        // query.searchTerm(), pageable);
        // } else {
        // }

        List<ActivityLog> toDomains = pageResult.getContent().stream().map(mapper::toDomain).toList();

        return new PagedResult<>(
                toDomains,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.isLast());
    }
    @Override
    public PagedResult<ActivityLog> findPageableByUserRoleLevelGreaterThanEqual(PageQuery query) {
        Pageable pageable = PageableHelper.toPageable(query);

        Page<ActivityLogEntity> pageResult = jpaRepository.findAllByUserRoleLevelGreaterThanEqual(pageable,SecurityUtils.getCurrentRole().getLevel());
        List<ActivityLog> toDomains = pageResult.getContent().stream().map(mapper::toDomain).toList();

        return new PagedResult<>(
                toDomains,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.isLast());
    }

    @Override
    public ActivityLog save(ActivityLog domain) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(SecurityUtils.getCurrentUser().getId());
        userEntity.setEmail(SecurityUtils.getCurrentUser().getEmail());
        ActivityLogEntity activity = mapper.toEntity(domain);
        activity.setUser(userEntity);
        activity.setActionBy(userEntity.getEmail());
        return mapper.toDomain(jpaRepository.save(activity));
    }

}
