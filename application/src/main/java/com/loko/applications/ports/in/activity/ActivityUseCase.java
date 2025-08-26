package com.loko.applications.ports.in.activity;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.activity.ActivityLogDto;
import com.loko.applications.dto.activity.ActivityLogRequestDto;

public interface ActivityUseCase {
    PagedResult<ActivityLogDto> dataTable(PageQuery query);
    PagedResult<ActivityLogDto> dataTableByUserRoleLevelGreaterEqual(PageQuery query);
    ActivityLogDto create(ActivityLogRequestDto dto);
}
