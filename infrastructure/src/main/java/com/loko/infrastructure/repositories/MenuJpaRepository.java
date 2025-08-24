package com.loko.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.loko.infrastructure.entities.MenuEntity;

public interface MenuJpaRepository extends JpaRepository<MenuEntity,UUID>{
    List<MenuEntity> findByParentIdIsNullOrderByDisplayOrderAsc();
    Page<MenuEntity> findByParentIdIsNullAndTitleContainingIgnoreCaseOrderByDisplayOrderAsc(String title,Pageable page);
    
}
