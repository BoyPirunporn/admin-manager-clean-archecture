package com.loko.infrastructure.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loko.applications.dto.auth.AuthRequestDto;
import com.loko.applications.dto.auth.AuthResponseDto;
import com.loko.applications.dto.auth.ChangePasswordDto;
import com.loko.applications.ports.in.auth.AuthUseCase;
import com.loko.applications.ports.in.role.RoleUseCase;
import com.loko.applications.ports.out.user.UserRepositoryPort;
import com.loko.domain.User;
import com.loko.domain.exception.BadRequestException;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.domain.exception.UnauthorizeException;
import com.loko.infrastructure.security.CustomUserDetails;
import com.loko.infrastructure.security.JwtService;
import com.loko.infrastructure.security.SecurityUtils;

@Service
public class AuthService implements AuthUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RoleUseCase roleUseCase;

    public AuthService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService, JwtService jwtService, RoleUseCase roleUseCase) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.roleUseCase = roleUseCase;
    }

    @Override
    public AuthResponseDto login(AuthRequestDto dto) {
        User user = userRepositoryPort.findByEmail(dto.email()).orElse(null);

        if (user == null || !passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new UnauthorizeException("Email or password incorrect");
        }
        String token = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new AuthResponseDto(
                token,
                refreshToken,
                user.getFirstName(),
                user.getLastName(),
                null,
                user.getRole().getName(),
                roleUseCase.getRoleById(user.getRole().getId()).permissions());

    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        CustomUserDetails userEntity = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(refreshToken, userEntity.getUsername())) {
            throw new UnauthorizeException("Invalid RefreshToken");
        }
        String newAccess = jwtService.generateToken(username);
        return AuthResponseDto.refreshToken(newAccess);
    }

    @Override
    @CacheEvict(value = "loadUser", key = "#email")
    public void logout(String email) {
        SecurityContextHolder.clearContext();
        logger.info("Evicting cache for user: {}", email);

    }

    @Override
    public void changePassword(ChangePasswordDto dto) {
        User user = userRepositoryPort.findByEmail(SecurityUtils.getCurrentUser().getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found in system."));
        if(!passwordEncoder.matches(dto.currentPassword(), user.getPassword())){
            throw new BadRequestException("Password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepositoryPort.save(user);
    }

}
