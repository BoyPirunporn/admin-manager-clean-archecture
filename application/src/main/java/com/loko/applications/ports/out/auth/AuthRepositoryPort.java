package com.loko.applications.ports.out.auth;

import com.loko.applications.dto.auth.AuthRequestDto;
import com.loko.domain.User;

public interface AuthRepositoryPort {
    User login(AuthRequestDto dto);
}
