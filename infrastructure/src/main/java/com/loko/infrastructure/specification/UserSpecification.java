package com.loko.infrastructure.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.loko.applications.dto.PageQuery;
import com.loko.infrastructure.entities.UserEntity;
import com.loko.infrastructure.security.SecurityUtils;

import jakarta.persistence.criteria.Predicate;

public class UserSpecification {
    public static Specification<UserEntity> filterCriteria(PageQuery query) {
        return (root, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            int roleLevel = SecurityUtils.getCurrentRole().getLevel();
            predicates.add(cb.greaterThanOrEqualTo(root.get("role").get("level"), roleLevel));

            String search = query.searchTerm();
            String column = query.searchByColumn();
            if (column != null && !column.isEmpty() && search != null && !search.trim().isEmpty()) {
                String pattern = search.toLowerCase() + "%";
                System.out.println("PATTERN -> " + pattern);
                predicates.add(cb.like(cb.lower(root.get(column)), pattern));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
