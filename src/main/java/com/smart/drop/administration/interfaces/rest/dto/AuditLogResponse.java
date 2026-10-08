package com.smart.drop.administration.interfaces.rest.dto;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Integer logId,
        Integer adminId,
        String action,
        LocalDateTime timestamp,
        String description
) {
}

