package com.smart.drop.inventory.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain entity for consumption records.
 */
public record Consumption(
        Integer consumptionId,
        Integer userId,
        BigDecimal volumeUsed,
        LocalDateTime dateRecorded,
        String usageContext
) {
    public Consumption {
        if (volumeUsed != null && volumeUsed.signum() < 0) {
            throw new IllegalArgumentException("volumeUsed cannot be negative");
        }
        if (usageContext != null && usageContext.length() > 50) {
            throw new IllegalArgumentException("usageContext must be <= 50 characters");
        }
    }

    public static Consumption create(Integer userId, BigDecimal volumeUsed, String usageContext) {
        return new Consumption(null, userId, volumeUsed, LocalDateTime.now(), usageContext);
    }
}

