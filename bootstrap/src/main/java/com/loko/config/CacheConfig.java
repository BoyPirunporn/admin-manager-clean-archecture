package com.loko.config;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.loko.applications.dto.PageQuery;

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

    @Bean
    public KeyGenerator customKeyGenerator() {
        return (target, method, params) -> {
            PageQuery input = (PageQuery) params[0];

            // global search value
            String globalSearch = input.searchTerm() != null ? input.searchTerm() : "";


            int key = Objects.hash(
                    input.pageNumber(),
                    input.pageSize(),
                    input.sortBy(),
                    input.sortDirection(),
                    input.searchByColumn(),
                    globalSearch
                    );
            System.out.println("KEY -> " + key);
            // รวม fields สำคัญทั้งหมดเป็น key
            return key;
        };
    }
}
