package com.loko.applications.dto;

import java.util.List;

public record PagedResult<T>(
    List<T> data,
    long totalElements,
    int totalPages,
    int pageNumber,
    int pageSize,
    boolean isLast
) {}
