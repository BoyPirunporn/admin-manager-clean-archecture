package com.loko.applications.ports.in.user;

import com.loko.applications.dto.auth.VerifyEmailRequestDto;
import com.loko.applications.dto.user.VerifyTokenResponseDto;

public interface VerificationUseCase {
    void verifyEmail(VerifyEmailRequestDto dto);
    VerifyTokenResponseDto verifyToken(String token);
}
