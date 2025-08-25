package com.loko.presentation.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.loko.applications.dto.ApiResponse;
import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.role.RoleCreationDto;
import com.loko.applications.dto.role.RoleDetailDto;
import com.loko.applications.dto.role.RoleDto;
import com.loko.applications.ports.in.role.RoleUseCase;
import com.loko.presentation.helper.PageablePresenHelper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${application.api.version}/roles")
public class RoleController {
    private final RoleUseCase roleUseCase;

    public RoleController(RoleUseCase roleUseCase) {
        this.roleUseCase = roleUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDto>>> getRoles() {
        return ResponseEntity.ok(ApiResponse.success(roleUseCase.getAllRoles(), 200));
    }

    @GetMapping("/dataTable")
    public ResponseEntity<PagedResult<RoleDto>> datatable(
            @PageableDefault(size = 10, sort = "name") Pageable pageable,
            @RequestParam(required = false) String search) {
        // 1. แปลง Pageable (ภาษาของ Spring) ไปเป็น PageQuery (ภาษากลาง)

        return ResponseEntity.ok(roleUseCase.dataTables(PageablePresenHelper.buildPageQuery(pageable, search)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<RoleDetailDto>> getById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(ApiResponse.success(roleUseCase.getRoleById(id), 200));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoleDto> postMethodName(@Valid @RequestBody RoleCreationDto dto) {
        return ResponseEntity.ok(roleUseCase.createRole(dto));
    }

}
