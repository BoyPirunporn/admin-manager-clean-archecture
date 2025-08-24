package com.loko.infrastructure.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.menu.MenuDto;
import com.loko.applications.dto.menu.MenuPermissionNodeDto;
import com.loko.applications.dto.menu.MenuRequestDto;
import com.loko.applications.ports.in.menu.MenuUseCase;
import com.loko.applications.ports.out.menu.MenuRepositoryPort;
import com.loko.applications.ports.out.role.RoleRepositoryPort;
import com.loko.domain.Menu;
import com.loko.domain.Role;
import com.loko.domain.RolePermission;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.infrastructure.mapper.MenuApiMapper;
import com.loko.infrastructure.security.SecurityUtils;

@Service
public class MenuService implements MenuUseCase {
    private final MenuRepositoryPort menuRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final MenuApiMapper menuApiMapper;

    public MenuService(MenuRepositoryPort menuRepositoryPort, RoleRepositoryPort roleRepositoryPort,
            MenuApiMapper menuApiMapper) {
        this.menuRepositoryPort = menuRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
        this.menuApiMapper = menuApiMapper;
    }

    @Override
    public MenuDto createMenu(MenuRequestDto menus) {
        return null;
    }

    @Override
    public List<MenuDto> getMenuHierarchy() {
        List<Menu> rootMenus = menuRepositoryPort.findRootMenus();
        return rootMenus.stream()
                .map(menuApiMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResult<MenuDto> dataTable(PageQuery query) {
        PagedResult<Menu> pageResult = menuRepositoryPort.findPageable(query);

        List<MenuDto> toDtos = pageResult.data().stream().map(menuApiMapper::toDto).toList();

        return new PagedResult<>(
                toDtos,
                pageResult.totalElements(),
                pageResult.totalPages(),
                pageResult.pageNumber(),
                pageResult.pageSize(),
                pageResult.isLast());
    }

    @Override
    public List<MenuPermissionNodeDto> getMenuByRoleId() {
        String roleId = SecurityUtils.getCurrentRole().getId().toString();
        Role role = roleRepositoryPort.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        Set<MenuPermissionNodeDto> permissionTree = buildMenuPermissionTree(role.getPermissions());
        return permissionTree.stream().toList();
    }

    private Set<MenuPermissionNodeDto> buildMenuPermissionTree(Set<RolePermission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
        }

        // Group children by their parent's ID
        Map<String, List<RolePermission>> parentToChildrenMap = permissions.stream()
                .filter(p -> p.getMenu().getParent() != null)
                .collect(Collectors.groupingBy(p -> p.getMenu().getParent().getId()));

        // Start building the tree from the root permissions (those without a parent)
        return permissions.stream()
                .filter(p -> p.getMenu().getParent() == null)
                .map(rootPermission -> mapToMenuPermissionNodeRecursive(rootPermission, parentToChildrenMap))
                .sorted(Comparator.comparingInt(p -> p.menuDisplayOrder()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private MenuPermissionNodeDto mapToMenuPermissionNodeRecursive(RolePermission permission,
            Map<String, List<RolePermission>> parentToChildrenMap) {
        // Find children for the current permission's menu, if any
        Set<MenuPermissionNodeDto> children = parentToChildrenMap
                .getOrDefault(permission.getMenu().getId(), Collections.emptyList())
                .stream()
                // Recursively map each child
                .map(childPermission -> mapToMenuPermissionNodeRecursive(childPermission, parentToChildrenMap))
                .sorted(Comparator.comparingInt(MenuPermissionNodeDto::menuDisplayOrder))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return menuApiMapper.toMenuPermissionNodeDto(permission, children);
    }
}
