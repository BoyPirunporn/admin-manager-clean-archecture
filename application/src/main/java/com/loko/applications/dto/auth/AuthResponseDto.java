package com.loko.applications.dto.auth;

import java.util.Set;

import com.loko.applications.dto.role.PermissionNodeDto;

public record AuthResponseDto(
    String token,
    String refreshToken,
    String firstName,
    String lastName,
    String image,
    String role,
    Set<PermissionNodeDto> permissions
) {
    public static AuthResponseDto refreshToken(String token){
        return new AuthResponseDto(token, null, null, null, null, null, null);
    }
}
