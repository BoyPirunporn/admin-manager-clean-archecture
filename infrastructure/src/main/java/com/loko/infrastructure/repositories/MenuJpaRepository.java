package com.loko.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loko.infrastructure.entities.MenuEntity;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, UUID> {
    List<MenuEntity> findByParentIdIsNullOrderByDisplayOrderAsc();

    @Query("""
                SELECT m FROM MenuEntity m
                WHERE m.parent IS NULL
                  AND (LOWER(m.nameEN) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(m.nameTH) LIKE LOWER(CONCAT('%', :keyword, '%')))
                ORDER BY m.displayOrder ASC
            """)
    Page<MenuEntity> findKeywordNameENOrNameTH(
            @Param("keyword") String keyword,
            Pageable page);

    @Query("SELECT m FROM MenuEntity m LEFT JOIN FETCH m.parent")
    List<MenuEntity> findAllWithParent();

}
