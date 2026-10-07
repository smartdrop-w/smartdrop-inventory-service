package com.smart.drop.administration.interfaces.rest.dto;

public record CreateAuditLogRequest(
        Integer adminId,
        String action,
        String description
) {
}

