package com.loko.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loko.infrastructure.repositories.UserJpaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserJpaRepository jpaRepository;


    public CustomUserDetailsService(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "loadUser",key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("LoadUserByUserName In CustomUserDetail");
        return jpaRepository.findByEmail(email).map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
    }
    
}
