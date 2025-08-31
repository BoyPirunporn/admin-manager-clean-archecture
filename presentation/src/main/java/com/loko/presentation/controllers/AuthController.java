package com.loko.presentation.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loko.applications.dto.ApiBaseResponse;
import com.loko.applications.dto.ApiResponse;
import com.loko.applications.dto.auth.AuthRequestDto;
import com.loko.applications.dto.auth.AuthResponseDto;
import com.loko.applications.dto.auth.ChangePasswordDto;
import com.loko.applications.dto.auth.VerifyEmailRequestDto;
import com.loko.applications.dto.user.VerifyTokenResponseDto;
import com.loko.applications.ports.in.auth.AuthUseCase;
import com.loko.applications.ports.in.user.VerificationUseCase;
import com.loko.domain.exception.UnauthorizeException;

import jakarta.validation.Valid;


@RestController
@RequestMapping("${application.api.version}/auth")
public class AuthController {
    private final AuthUseCase authUseCase;
    private final VerificationUseCase verificationUseCase;;

    public AuthController(AuthUseCase authUseCase, VerificationUseCase verificationUseCase) {
        this.authUseCase = authUseCase;
        this.verificationUseCase = verificationUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody AuthRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.success(authUseCase.login(dto), 200));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        authUseCase.changePassword(dto);
        return ResponseEntity.ok(ApiResponse.success("Password has been changed!", 200));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refreshToken(@RequestBody Map<String, String> dto) {
        if (!dto.containsKey("refreshToken")) {
            throw new UnauthorizeException("Invalid Refresh Token");
        }
        return ResponseEntity.ok(ApiResponse.success(authUseCase.refreshToken(dto.get("refreshToken")), 200));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(Authentication authentication) {
        if (authentication != null) {
            System.out.println("HAS SESSION");
            authUseCase.logout(authentication.getName());
        }
        return ResponseEntity
                .ok(ApiResponse.success("Logged out successfully. Please delete your token on the client side.", 200));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiBaseResponse> postMethodName(@Valid @RequestBody VerifyEmailRequestDto token) {
        verificationUseCase.verifyEmail(token);
        return ResponseEntity.ok(new ApiBaseResponse("Account verified! You can now use the system.",200));
    }

    @GetMapping("/verify-token")
    public ResponseEntity<ApiResponse<VerifyTokenResponseDto>> getMethodName(@RequestParam(name="token",required = true) String token) {
        return ResponseEntity.ok(ApiResponse.success(verificationUseCase.verifyToken(token), 200));
    }
    

}
