package com.loko.infrastructure.persistence.role;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.ports.out.role.RoleRepositoryPort;
import com.loko.domain.Role;
import com.loko.infrastructure.entities.RoleEntity;
import com.loko.infrastructure.helper.PageableHelper;
import com.loko.infrastructure.repositories.RoleJpaRepository;

@Component

public class RolePersistenceAdapter implements RoleRepositoryPort {
    private final RoleJpaRepository jpaRepository;
    private final RolePersistenceMapper mapper;

    public RolePersistenceAdapter(RoleJpaRepository jpaRepository, RolePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Role save(Role role) {
        RoleEntity roleEntity = mapper.toEntity(role);
        if (roleEntity.getPermissions() != null) {
            roleEntity.getPermissions().forEach(p -> {
                p.setRole(roleEntity);
            });
        }
        RoleEntity saveEntity = jpaRepository.save(roleEntity);
        return mapper.toDomain(saveEntity);
    }

    @Override
    public Optional<Role> findById(String id) {
        return jpaRepository.findById(UUID.fromString(id)).map(mapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Set<Role> findAllByIds(Set<String> ids) {
        return jpaRepository.findAllById(ids.stream().map(UUID::fromString).toList()).stream().map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, String id) {
        return jpaRepository.existsByNameAndIdNot(name, UUID.fromString(id));
    }

    @Override
    public PagedResult<Role> findPaginated(PageQuery query) {
        Pageable pageable = PageableHelper.toPageable(query);
        // 2. เรียกใช้ Repository เพื่อดึงข้อมูลแบบแบ่งหน้า
        Page<RoleEntity> pageResult;
        if (query.searchTerm() != null && !query.searchTerm().isBlank()) {
            pageResult = jpaRepository.findByNameContainingIgnoreCase(query.searchTerm(), pageable);
        } else {
            pageResult = jpaRepository.findAll(pageable);
        }

        List<Role> roleDomain = pageResult.getContent().stream().map(mapper::toDomain).toList();
        return new PagedResult<>(
                roleDomain,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.isLast());
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public List<Role> saveAll(List<Role> roles) {
        List<RoleEntity> entities = roles.stream().map(mapper::toEntity).toList();
        return jpaRepository.saveAll(entities).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Role> findByNameWithPermissions(String name) {
        return jpaRepository.findByNameWithPermissions(name).map(mapper::toDomain);
    }

}
