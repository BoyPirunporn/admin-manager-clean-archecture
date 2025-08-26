package com.loko.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.loko.infrastructure.entities.ActivityLogEntity;

public interface ActivityJpaRepository extends JpaRepository<ActivityLogEntity,UUID> {
    Page<ActivityLogEntity> findAllByUserRoleLevelGreaterThanEqual(Pageable page,int level);
}
