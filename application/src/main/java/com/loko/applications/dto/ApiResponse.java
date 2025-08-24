package com.loko.applications.dto;

public record ApiResponse<T>(
        boolean success,
        int status,
        T payload) {
    public static <T> ApiResponse<T> success(T payload, int status) {
        return new ApiResponse<>(true, status, payload);
    }

}
