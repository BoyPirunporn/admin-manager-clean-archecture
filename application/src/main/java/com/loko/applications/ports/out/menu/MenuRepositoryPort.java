package com.loko.applications.ports.out.menu;

import java.util.List;
import java.util.Set;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.menu.MenuPermissionNodeDto;
import com.loko.domain.Menu;

public interface MenuRepositoryPort {
    List<Menu> findRootMenus();

    PagedResult<Menu> findPageable(PageQuery query);

    // List<Menu> findMenuByRoleId(Set<String> roleIds);
    List<Menu> findAllByIds(Set<String> id);

    List<MenuPermissionNodeDto> findMenuWithRoleId();

    Menu save(Menu menu);

    List<Menu> saveAll(List<Menu> menu);
    List<Menu> findAll();
    List<Menu> findAllWithParent();

    long count();
}
