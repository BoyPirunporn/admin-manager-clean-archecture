package com.loko.applications.ports.in.auth;

import com.loko.applications.dto.auth.AuthRequestDto;
import com.loko.applications.dto.auth.AuthResponseDto;
import com.loko.applications.dto.auth.ChangePasswordDto;

public interface AuthUseCase {
    AuthResponseDto login(AuthRequestDto dto);
    AuthResponseDto refreshToken(String refreshToken);
    void logout(String username);
    void changePassword(ChangePasswordDto dto);
}
