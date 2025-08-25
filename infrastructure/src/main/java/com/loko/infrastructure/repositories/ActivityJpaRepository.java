package com.loko.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loko.infrastructure.entities.ActivityLogEntity;

public interface ActivityJpaRepository extends JpaRepository<ActivityLogEntity,UUID> {
    
}
