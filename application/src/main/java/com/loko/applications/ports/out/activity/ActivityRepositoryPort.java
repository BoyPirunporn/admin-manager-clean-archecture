package com.loko.applications.ports.out.activity;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.domain.ActivityLog;

public interface ActivityRepositoryPort {
    PagedResult<ActivityLog> findPageable(PageQuery query);
    PagedResult<ActivityLog> findPageableByUserRoleLevelGreaterThanEqual(PageQuery query);
    ActivityLog save(ActivityLog domain);
}
