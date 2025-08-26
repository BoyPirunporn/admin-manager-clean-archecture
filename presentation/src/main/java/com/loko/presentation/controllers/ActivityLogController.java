package com.loko.presentation.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.loko.applications.dto.ApiResponse;
import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.activity.ActivityLogDto;
import com.loko.applications.dto.activity.ActivityLogRequestDto;
import com.loko.applications.ports.in.activity.ActivityUseCase;
import com.loko.presentation.helper.PageablePresenHelper;

@RestController
@RequestMapping("${application.api.version}/activity-logs")
public class ActivityLogController {
    private final ActivityUseCase activityUseCase;

    public ActivityLogController(ActivityUseCase activityUseCase) {
        this.activityUseCase = activityUseCase;
    }

    @GetMapping("/dataTable")
    public ResponseEntity<PagedResult<ActivityLogDto>> dataTable(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable,
            @RequestParam(required = false) String search) {
                PageQuery query = PageablePresenHelper.buildPageQuery(pageable,search);
        return ResponseEntity.ok(activityUseCase.dataTableByUserRoleLevelGreaterEqual(query));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<String>> create(@RequestBody ActivityLogRequestDto dto) {
        activityUseCase.create(dto);
        return ResponseEntity.ok(ApiResponse.success("Activity has been created", 201));
    }

}
