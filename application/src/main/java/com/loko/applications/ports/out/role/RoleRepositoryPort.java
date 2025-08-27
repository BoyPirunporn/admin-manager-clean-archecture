package com.loko.applications.ports.out.role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.domain.Role;

public interface RoleRepositoryPort {
    Role save(Role role);
    List<Role> saveAll(List<Role> role);
    Optional<Role> findById(String id);
    Optional<Role> findByName(String name);
    Optional<Role> findByNameWithPermissions(String name);
    List<Role> findAll();
    Set<Role> findAllByIds(Set<String> ids); // Added this method
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, String id);
    PagedResult<Role> findPaginated(PageQuery page);
    long count();
}
