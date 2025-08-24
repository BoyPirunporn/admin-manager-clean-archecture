package com.loko.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.loko.infrastructure.entities.RoleEntity;
import com.loko.infrastructure.entities.UserEntity;

public class SecurityUtils {
    public static UserEntity getCurrentUser() {
        Authentication authenticator = SecurityContextHolder.getContext().getAuthentication();
         if (authenticator != null && authenticator.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        return null;
    }

    public static RoleEntity getCurrentRole() {
        Authentication authenticator = SecurityContextHolder.getContext().getAuthentication();
        if (authenticator != null && authenticator.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser().getRole();
        }
        return null;
    }
}
