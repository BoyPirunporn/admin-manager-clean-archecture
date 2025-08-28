package com.loko.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loko.infrastructure.entities.VerificationTokenEntity;

public interface VerificationJpaRepository extends JpaRepository<VerificationTokenEntity,UUID> {
    Optional<VerificationTokenEntity> findByToken(String token);
    boolean existsByToken(String token);
}
