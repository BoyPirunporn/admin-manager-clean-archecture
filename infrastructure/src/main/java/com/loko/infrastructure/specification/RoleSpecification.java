package com.loko.infrastructure.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.loko.applications.dto.PageQuery;
import com.loko.infrastructure.entities.RoleEntity;

import jakarta.persistence.criteria.Predicate;

public class RoleSpecification {

    public static Specification<RoleEntity> filterCriterial(PageQuery pageQuery) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String searchTerm = pageQuery.searchTerm();
            String column = pageQuery.searchByColumn();
            if (column != null && !column.isEmpty() && searchTerm != null && !searchTerm.trim().isEmpty()) {
                String pattern = searchTerm.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(pageQuery.searchByColumn())),
                    pattern
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
