package com.loko.applications.ports.in.auth;

import com.loko.applications.dto.auth.AuthRequestDto;
import com.loko.applications.dto.auth.AuthResponseDto;

public interface AuthUseCase {
    AuthResponseDto login(AuthRequestDto dto);
    AuthResponseDto refreshToken(String refreshToken);
    void logout(String username);
}
