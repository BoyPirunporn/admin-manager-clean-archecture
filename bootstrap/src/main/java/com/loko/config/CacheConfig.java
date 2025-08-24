package com.loko.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {
    
    @Bean
    CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // ตั้งค่าให้ Cache ที่ชื่อ "users" หมดอายุหลังจากเขียนไปแล้ว 10 นาที
        cacheManager.registerCustomCache("loadUser", 
            Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build()
        );

        return cacheManager;

    }
}
