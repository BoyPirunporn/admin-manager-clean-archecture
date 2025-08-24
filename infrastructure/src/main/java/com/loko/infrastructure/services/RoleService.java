package com.loko.infrastructure.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.role.PermissionNodeDto;
import com.loko.applications.dto.role.PermissionUpdateDto;
import com.loko.applications.dto.role.RoleCreationDto;
import com.loko.applications.dto.role.RoleDetailDto;
import com.loko.applications.dto.role.RoleDto;
import com.loko.applications.ports.in.role.RoleUseCase;
import com.loko.applications.ports.out.menu.MenuRepositoryPort;
import com.loko.applications.ports.out.role.RoleRepositoryPort;
import com.loko.domain.Menu;
import com.loko.domain.Role;
import com.loko.domain.RolePermission;
import com.loko.domain.exception.DuplicateResourceException;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.infrastructure.mapper.PermissionApiMapper;
import com.loko.infrastructure.mapper.RoleApiMapper;

@Service
@Transactional
public class RoleService implements RoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;
    private final MenuRepositoryPort menuRepository; // Injected for fetching menu details

    private final RoleApiMapper mapper;
    private final PermissionApiMapper permissionMapper;

    public RoleService(RoleRepositoryPort roleRepositoryPort, MenuRepositoryPort menuRepository, RoleApiMapper mapper,
            PermissionApiMapper permissionMapper) {
        this.roleRepositoryPort = roleRepositoryPort;
        this.menuRepository = menuRepository;
        this.mapper = mapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public RoleDto createRole(RoleCreationDto dto) {
        if (roleRepositoryPort.existsByName(dto.name())) {
            throw new DuplicateResourceException("Role with name '" + dto.name() + "' already exists.");
        }
        Role role = mapper.toRole(dto);
        // REFACTORED: Business logic now resides in the service layer.
        Set<RolePermission> processedPermissions = processPermissions(dto.permissions());
        role.setPermissions(processedPermissions);
        Role saveRole = roleRepositoryPort.save(role);
        return mapper.toRoleDto(saveRole);

    }

    @Override
    @Transactional(readOnly = true)
    public RoleDetailDto getRoleById(String id) {
        Role role = roleRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        Set<PermissionNodeDto> permissionTree = role.getPermissions().stream()
        .filter(p -> !p.getMenu().isGroup())
        .sorted(Comparator.comparingInt(p -> p.getMenu().getDisplayOrder()))
        .map(p -> {
            return permissionMapper.toPermissionNodeDto(p, null);
        }).collect(Collectors.toSet());

        return new RoleDetailDto(role.getId(), role.getName(), role.getDescription(), permissionTree);
    }

    // private Set<PermissionNodeDto> buildPermissionTree(Set<RolePermission> permissions) {
    //     if (permissions == null || permissions.isEmpty()) {
    //         return Collections.emptySet();
    //     }
        
    //     // Group children by their parent's ID
    //     Map<String, List<RolePermission>> parentToChildrenMap = permissions.stream()
    //             .filter(p -> p.getMenu().getParent() != null)
    //             .collect(Collectors.groupingBy(p -> p.getMenu().getParent().getId()));

    //     // Start building the tree from the root permissions (those without a parent)
    //     return permissions.stream()
    //             .filter(p -> p.getMenu().getParent() != null)
    //             .map(rootPermission -> mapToPermissionNodeRecursive(rootPermission, parentToChildrenMap))
    //             .collect(Collectors.toCollection(LinkedHashSet::new));
    // }

    // private PermissionNodeDto mapToPermissionNodeRecursive(RolePermission permission,
    //         Map<String, List<RolePermission>> parentToChildrenMap) {
    //     // Find children for the current permission's menu, if any
    //     Set<PermissionNodeDto> children = parentToChildrenMap
    //             .getOrDefault(permission.getMenu().getId(), Collections.emptyList())
    //             .stream()
    //             // Recursively map each child
    //             .map(childPermission -> mapToPermissionNodeRecursive(childPermission, parentToChildrenMap))
    //             .collect(Collectors.toCollection(LinkedHashSet::new));

    //     return permissionMapper.toPermissionNodeDto(permission, new HashSet<>());
    // }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepositoryPort.findAll().stream().map(mapper::toRoleDto).collect(Collectors.toList());
    }

    @Override
    public RoleDetailDto updateRole(String roleId, RoleCreationDto roleCreationDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRole'");
    }

    private Set<RolePermission> processPermissions(Set<PermissionUpdateDto> permissionDtos) {
        // 1. Get all unique menu IDs from the DTO
        Set<String> menuIds = permissionDtos.stream()
                .map(PermissionUpdateDto::menuId)
                .collect(Collectors.toSet());

        // 2. Fetch all relevant menu domain objects from the DB in one go
        Map<String, Menu> menuMap = menuRepository.findAllByIds(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));

        // 3. Use a Map to ensure unique permissions per menu
        Map<String, RolePermission> finalPermissionsMap = new HashMap<>();

        for (PermissionUpdateDto dto : permissionDtos) {
            Menu menu = menuMap.get(dto.menuId());
            if (menu == null) {
                // Optionally throw an exception or log a warning for invalid menu ID
                continue;
            }

            // Add the permission for the child menu
            finalPermissionsMap.put(menu.getId(), permissionMapper.toRolePermission(dto));

            // Business Logic: If a child has any permission, its parent must have at least
            // view permission
            Menu parent = menu.getParent();
            if (parent != null) {
                // Use computeIfAbsent to add the parent permission only if it doesn't exist
                finalPermissionsMap.computeIfAbsent(parent.getId(), parentId -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setCanCreate(false);
                    rolePermission.setCanUpdate(false);
                    rolePermission.setCanDelete(false);
                    rolePermission.setCanView(true);
                    rolePermission.setMenu(parent);
                    return rolePermission;
                });
            }
        }
        return new HashSet<>(finalPermissionsMap.values());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "role-datatable", key = "#query")
    public PagedResult<RoleDto> dataTables(PageQuery query) {
        PagedResult<Role> pageResult = roleRepositoryPort.findPaginated(query);

        List<RoleDto> roleDtos = pageResult.data().stream().map(mapper::toRoleDto).toList();
        return new PagedResult<>(
                roleDtos,
                pageResult.totalElements(),
                pageResult.totalPages(),
                pageResult.pageNumber(),
                pageResult.pageSize(),
                pageResult.isLast());
    }

}
