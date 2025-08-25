package com.loko.presentation.helper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.loko.applications.dto.PageQuery;

public class PageablePresenHelper {
     public static PageQuery buildPageQuery(Pageable pageable) {
        PageQuery query = new PageQuery(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().stream().findFirst().map(Sort.Order::getProperty).orElse(null),
                pageable.getSort().stream().findFirst().map(Sort.Order::getDirection).orElse(Sort.Direction.ASC).name(),
                null);
        return query;
    }
    public static PageQuery buildPageQuery(Pageable pageable,String search) {
        PageQuery query = new PageQuery(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().stream().findFirst().map(Sort.Order::getProperty).orElse(null),
                pageable.getSort().stream().findFirst().map(Sort.Order::getDirection).orElse(Sort.Direction.ASC).name(),
                search);
        return query;
    }
}
