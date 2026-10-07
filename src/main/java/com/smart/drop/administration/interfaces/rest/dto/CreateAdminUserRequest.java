package com.smart.drop.administration.interfaces.rest.dto;

public record CreateAdminUserRequest(
        String username,
        String email,
        String role
) {
}

