package com.loko.applications.dto;

public record PageQuery(
    int pageNumber,
    int pageSize,
    String sortBy,
    String sortDirection,
    String searchTerm,
    String searchByColumn
) {}