package com.loko.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.loko.infrastructure.entities.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity,UUID> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findAllByFirstNameContainingIgnoreCaseOrderById(String firstName,Pageable pageable);
}
