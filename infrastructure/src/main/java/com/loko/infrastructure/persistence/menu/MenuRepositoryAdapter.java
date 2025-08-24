package com.loko.infrastructure.persistence.menu;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.menu.MenuPermissionNodeDto;
import com.loko.applications.ports.out.menu.MenuRepositoryPort;
import com.loko.domain.Menu;
import com.loko.infrastructure.entities.MenuEntity;
import com.loko.infrastructure.helper.PageableHelper;
import com.loko.infrastructure.repositories.MenuJpaRepository;

@Component
public class MenuRepositoryAdapter implements MenuRepositoryPort {
    private final MenuJpaRepository jpaRepository;
    private final MenuPersistenceMapper mapper;

    public MenuRepositoryAdapter(MenuJpaRepository jpaRepository, MenuPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Menu> findRootMenus() {
        return jpaRepository.findByParentIdIsNullOrderByDisplayOrderAsc().stream()
                .map(e -> {
                    System.out.println(e.isGroup());
                    return mapper.toDomain(e);
                })
                .collect(Collectors.toList());
    }

    // @Override
    // @Transactional(readOnly = true)
    // public List<Menu> findMenuByRoleIds(Set<String> roleIds) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'findMenuByRoleIds'");
    // }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> findAllByIds(Set<String> id) {
        return jpaRepository.findAllById(id.stream().map(UUID::fromString).toList()).stream().map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResult<Menu> findPageable(PageQuery query) {
        Pageable pageable = PageableHelper.toPageable(query);

        Page<MenuEntity> pageResult;
        if (query.searchTerm() != null && !query.searchTerm().isBlank()) {
            pageResult = jpaRepository.findByParentIdIsNullAndTitleContainingIgnoreCaseOrderByDisplayOrderAsc(
                    query.searchTerm(), pageable);
        } else {
            pageResult = jpaRepository.findAll(pageable);
        }

        List<Menu> toDomains = pageResult.getContent().stream().map(mapper::toDomain).toList();

        return new PagedResult<>(
                toDomains,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.isLast());
    }

    @Override
    public List<MenuPermissionNodeDto> findMenuWithRoleId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findMenuWithRoleId'");
    }

}
