package com.loko.infrastructure.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.loko.applications.dto.PageQuery;

public class PageableHelper {
    public static Pageable toPageable(PageQuery query) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(query.sortDirection()), query.sortBy());
        Pageable pageable = PageRequest.of(query.pageNumber(), query.pageSize(), sort);
        return pageable;
    }

   
  
}
