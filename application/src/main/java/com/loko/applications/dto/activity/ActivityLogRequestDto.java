package com.loko.applications.dto.activity;

public record ActivityLogRequestDto(
        String id,
        String action,
        String target,
        String metadata,
        String ipAddress) {
}