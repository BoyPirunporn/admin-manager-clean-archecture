package com.loko.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loko.infrastructure.entities.MenuEntity;

public interface MenuJpaRepository extends JpaRepository<MenuEntity,UUID>{
    List<MenuEntity> findByParentIdIsNullOrderByDisplayOrderAsc();
    Page<MenuEntity> findByParentIdIsNullAndTitleContainingIgnoreCaseOrderByDisplayOrderAsc(String title,Pageable page);
    
    @Query("SELECT m FROM MenuEntity m LEFT JOIN FETCH m.parent")
    List<MenuEntity> findAllWithParent();
    
}
