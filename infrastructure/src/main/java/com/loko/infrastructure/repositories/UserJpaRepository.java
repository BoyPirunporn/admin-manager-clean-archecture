package com.loko.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.loko.infrastructure.entities.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity,UUID>,JpaSpecificationExecutor<UserEntity> {
    boolean existsByEmail(String email);
    boolean existsByRole_Name(String roleName);

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findAllByFirstNameContainingIgnoreCaseAndRole_LevelGreaterThanEqualOrderById(String firstName,int level,Pageable pageable);
    Page<UserEntity> findAllByRole_LevelGreaterThanEqualOrderByCreatedAt(int level,Pageable pageable);
}
