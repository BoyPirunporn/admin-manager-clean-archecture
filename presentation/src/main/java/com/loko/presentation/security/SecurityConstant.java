package com.loko.presentation.security;

import java.util.List;

public class SecurityConstant {
     public static final List<String> PUBLIC_ROUTE = List.of(
            "/files/**",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh-token",
            "/swagger-ui/**",
            "/swagger-ui.html", // ✅ เพิ่ม Swagger UI HTML
            "/v3/api-docs/**", // ✅ อนุญาต API Docs ทั้งหมด
            "/*.ico"
    );
}
