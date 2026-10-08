package com.smart.drop.administration.domain.model.entities;

import java.time.LocalDateTime;

/**
 * Immutable domain entity for administrative users.
 */
public record AdminUser(
        Integer adminId,
        String username,
        String email,
        String role,
        LocalDateTime createdAt
) {
    public AdminUser {
        if (username != null && username.length() > 50) {
            throw new IllegalArgumentException("username must be <= 50 characters");
        }
        if (email != null && email.length() > 150) {
            throw new IllegalArgumentException("email must be <= 150 characters");
        }
        if (role != null && role.length() > 50) {
            throw new IllegalArgumentException("role must be <= 50 characters");
        }
    }

    public static AdminUser create(String username, String email, String role) {
        return new AdminUser(null, username, email, role, LocalDateTime.now());
    }

    public AdminUser withRole(String newRole) {
        return new AdminUser(adminId, username, email, newRole, createdAt);
    }
}

