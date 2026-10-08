package com.smart.drop.administration.interfaces.rest.dto;

import java.time.LocalDateTime;

public record AdminUserResponse(
        Integer adminId,
        String username,
        String email,
        String role,
        LocalDateTime createdAt
) {
}

