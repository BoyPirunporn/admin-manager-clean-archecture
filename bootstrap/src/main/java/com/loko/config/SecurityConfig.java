package com.loko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.loko.presentation.security.JwtAuthenticationFilter;
import com.loko.presentation.security.SecurityConstant;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // ตรวจสอบว่าคุณมี PasswordEncoder
        return authProvider;
    }

    @Bean
    SecurityFilterChain doFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
                .authorizeHttpRequests(
                        a -> a.requestMatchers(SecurityConstant.PUBLIC_ROUTE.toArray(new String[0])).permitAll()
                                .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((accessDenied) -> {
                    accessDenied.accessDeniedHandler((request, response, accessDeniedException) -> {
                        System.out.println("MESSAGE ACCESS DENIED : " + request.getRequestURI());
                        System.out.println("MESSAGE ACCESS DENIED : " + accessDeniedException.getMessage());
                        accessDeniedException.printStackTrace();
                        response.setStatus(403);
                        response.getWriter().write(accessDeniedException.getMessage());
                    });
                });
        ;

        return http.build();
    }
}
