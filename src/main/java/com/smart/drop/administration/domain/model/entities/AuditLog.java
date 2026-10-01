package com.smart.drop.administration.domain.model.entities;

import java.time.LocalDateTime;

/**
 * Immutable domain entity for audit logs.
 */
public record AuditLog(
        Integer logId,
        Integer adminId,
        String action,
        LocalDateTime timestamp,
        String description
) {
    public AuditLog {
        if (action != null && action.length() > 100) {
            throw new IllegalArgumentException("action must be <= 100 characters");
        }
    }

    public static AuditLog create(Integer adminId, String action, String description) {
        return new AuditLog(null, adminId, action, LocalDateTime.now(), description);
    }
}

