package com.loko.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loko.infrastructure.entities.RoleEntity;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    Page<RoleEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<RoleEntity> findByName(String name);

    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.permissions p LEFT JOIN FETCH p.menu m LEFT JOIN FETCH m.parent WHERE r.name = :roleName")
    Optional<RoleEntity> findByNameWithPermissions(@Param("roleName") String roleName);
}
