package com.loko.infrastructure.persistence.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.ports.out.user.UserRepositoryPort;
import com.loko.domain.User;
import com.loko.infrastructure.entities.UserEntity;
import com.loko.infrastructure.helper.PageableHelper;
import com.loko.infrastructure.repositories.UserJpaRepository;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper mapper;

    

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository, UserPersistenceMapper mapper) {
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saveEntity = userJpaRepository.save(entity);
        return mapper.toDomain(saveEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public PagedResult<User> findPageable(PageQuery query) {
        Pageable pageable = PageableHelper.toPageable(query);

        Page<UserEntity> pageResult;
        if (query.searchTerm() != null && !query.searchTerm().isBlank()) {
            pageResult = userJpaRepository.findAllByFirstNameContainingIgnoreCaseOrderById(query.searchTerm(), pageable);
        } else {
            pageResult = userJpaRepository.findAll(pageable);
        }

        List<User> toDomains = pageResult.getContent().stream().map(mapper::toDomain).toList();

        return new PagedResult<>(
                toDomains,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.isLast());
    }

    @Override
    public Optional<User> findById(String id) {
        return userJpaRepository.findById(UUID.fromString(id)).map(mapper::toDomain);
    }

    @Override
    public boolean existsByRole_Name(String roleName) {
        return userJpaRepository.existsByRole_Name(roleName);
    }
    
}
