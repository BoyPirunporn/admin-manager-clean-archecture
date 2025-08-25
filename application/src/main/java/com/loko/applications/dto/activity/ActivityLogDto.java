package com.loko.applications.dto.activity;

import java.time.LocalDateTime;

public record ActivityLogDto(
        String id,
        String action,
        String target,
        String metadata,
        String ipAddress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}