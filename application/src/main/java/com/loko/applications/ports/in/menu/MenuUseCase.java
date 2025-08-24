package com.loko.applications.ports.in.menu;

import java.util.List;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.menu.MenuDto;
import com.loko.applications.dto.menu.MenuPermissionNodeDto;
import com.loko.applications.dto.menu.MenuRequestDto;

public interface MenuUseCase {
    MenuDto createMenu(MenuRequestDto request);

    List<MenuDto> getMenuHierarchy();
    List<MenuPermissionNodeDto> getMenuByRoleId();
    PagedResult<MenuDto> dataTable(PageQuery query);
}
