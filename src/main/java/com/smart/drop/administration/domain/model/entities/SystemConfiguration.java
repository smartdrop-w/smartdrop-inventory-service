package com.smart.drop.administration.domain.model.entities;

import java.time.LocalDateTime;

/**
 * Immutable domain entity for system configuration values.
 */
public record SystemConfiguration(
        Integer configId,
        String parameterKey,
        String parameterValue,
        LocalDateTime updatedAt
) {
    public SystemConfiguration {
        if (parameterKey == null || parameterKey.isBlank()) {
            throw new IllegalArgumentException("parameterKey is required");
        }
        if (parameterKey.length() > 100) {
            throw new IllegalArgumentException("parameterKey must be <= 100 characters");
        }
        if (parameterValue == null || parameterValue.isBlank()) {
            throw new IllegalArgumentException("parameterValue is required");
        }
        if (parameterValue.length() > 255) {
            throw new IllegalArgumentException("parameterValue must be <= 255 characters");
        }
    }

    public static SystemConfiguration create(String parameterKey, String parameterValue) {
        return new SystemConfiguration(null, parameterKey, parameterValue, LocalDateTime.now());
    }

    public SystemConfiguration withValue(String newValue) {
        return new SystemConfiguration(configId, parameterKey, newValue, LocalDateTime.now());
    }
}

