package com.loko.applications.ports.out.user;

import java.util.Optional;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.domain.User;

public interface UserRepositoryPort {
    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);

    PagedResult<User> findPageable(PageQuery query);
}
