package com.loko.presentation.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.loko.applications.jwt.JwtUseCase;
import com.loko.domain.exception.JwtException;
import com.loko.domain.exception.UnauthorizeException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Autowired
    private JwtUseCase jwtUseCase;
    @Autowired
    private UserDetailsService userDetailsService;

    protected boolean shouldNotFilter(@SuppressWarnings("null") HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // log.info("MATCH : " + EXCLUDED_URLS.stream().anyMatch(path::matches));

        return SecurityConstant.PUBLIC_ROUTE.stream().anyMatch(url -> {
            if (url.endsWith("/**")) {
                // รองรับ wildcard แบบ /swagger-ui/**
                String base = url.substring(0, url.length() - 3);
                return path.startsWith(base);
            } else if (url.endsWith("/*")) {
                // รองรับ wildcard แบบ /api/*
                String base = url.substring(0, url.length() - 2);
                return path.startsWith(base);
            } else {
                // match path ตรง ๆ
                return path.equals(url);
            }
        });
    }

    @Override
    protected void doFilterInternal(
            @SuppressWarnings("null") HttpServletRequest request,
            @SuppressWarnings("null") HttpServletResponse response,
            @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("[ REQUEST URI ] {}", request.getRequestURI());
        if (shouldNotFilter(request)) {
            logger.info("SHOULD NOT FILTER");
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("SHOULD FILTER");

        // log.info("URI " + uri);
        String headerToken = request.getHeader("Authorization");
        logger.info("headerToken -> {}", headerToken);
        if (isTokenInvalid(headerToken)) {
            resolver.resolveException(request, response, null,
                    new UnauthorizeException("Unauthorized"));
            return;
        }

        logger.info("Check token");
        try {
            final String token = extractToken(headerToken);
            logger.info("EXTRACT TOKEN");
            String username = jwtUseCase.extractUsername(token);
            logger.info("EXTRACT USERNAME");
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.info("HANDLE AUTHENTICATION ");
                handleValidToken(username, token, request);
            }
            logger.info("Allow Access Token -> {}", username);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.info(e.getMessage());
            resolver.resolveException(request, response, null,
                    new UnauthorizeException(e.getMessage()));
            return;
        }
    }

    public boolean isTokenInvalid(String token) {
        return token == null || !token.startsWith("Bearer");
    }

    public String extractToken(String header) {
        return header.substring(7);
    }

    public void handleValidToken(String username, String token, HttpServletRequest request) {
        logger.info("GET USER DETAIL");
        UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);
        logger.info("USER DETAIL IS -> {}", userDetails.getUsername());
        if (jwtUseCase.isTokenValid(token, userDetails.getUsername())) {
            logger.info("Token is valid -> {}", userDetails.getUsername());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
            logger.info("Initial User Token -> {}", userDetails.getUsername());
            userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            logger.info("Set User Token -> {}", userDetails.getUsername());
            SecurityContextHolder.getContext().setAuthentication(userToken);
            logger.info("Set SecurityContext -> {}", userDetails.getUsername());
        }
    }
   
    public void handleInvalidToken(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Unimplemented method 'handleInvalidToken'");
    }

}
