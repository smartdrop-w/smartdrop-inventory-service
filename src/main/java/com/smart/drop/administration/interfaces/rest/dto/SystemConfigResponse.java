package com.smart.drop.administration.interfaces.rest.dto;

import java.time.LocalDateTime;

public record SystemConfigResponse(
        Integer configId,
        String parameterKey,
        String parameterValue,
        LocalDateTime updatedAt
) {
}

