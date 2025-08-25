package com.loko.infrastructure.persistence.verification;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loko.applications.ports.out.verification.VerificationTokenRepositoryPort;
import com.loko.domain.VerificationToken;
import com.loko.infrastructure.entities.VerificationTokenEntity;
import com.loko.infrastructure.repositories.VerificationJpaRepository;

@Component
public class VerificationPersistenceAdapter implements VerificationTokenRepositoryPort {
    private final VerificationJpaRepository repository;
    private final VerificationPersistenceMapper mapper;

    public VerificationPersistenceAdapter(VerificationJpaRepository repository, VerificationPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public VerificationToken save(VerificationToken dto) {
        VerificationTokenEntity entity = mapper.toEntity(dto);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return repository.findByToken(token).map(mapper::toDomain);
    }

    @Override
    public void delete(VerificationToken token) {
        repository.delete(mapper.toEntity(token));
    }

}
