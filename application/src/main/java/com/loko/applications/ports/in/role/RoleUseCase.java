package com.loko.applications.ports.in.role;

import java.util.List;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.role.RoleCreationDto;
import com.loko.applications.dto.role.RoleDetailDto;
import com.loko.applications.dto.role.RoleDto;

public interface RoleUseCase {
    RoleDto createRole(RoleCreationDto create);
    RoleDetailDto getRoleById(String id);
    List<RoleDto> getAllRoles();
    PagedResult<RoleDto> dataTables(PageQuery query);
    RoleDetailDto updateRole(String roleId, RoleCreationDto roleCreationDto);
}
