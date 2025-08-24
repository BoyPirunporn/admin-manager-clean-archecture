package com.loko.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.loko.infrastructure.entities.RoleEntity;

public interface RoleJpaRepository extends JpaRepository<RoleEntity,UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
    Page<RoleEntity> findByNameContainingIgnoreCase(String name,Pageable pageable);
}
