package com.smart.drop.inventory.domain.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Immutable domain entity for inventory records.
 */
public record Inventory(
        Integer inventoryId,
        Integer userId,
        BigDecimal availableLiters,
        LocalDate lastRefillDate,
        LocalDate nextRefillPrediction
) {
    public Inventory {
        if (availableLiters != null && availableLiters.signum() < 0) {
            throw new IllegalArgumentException("availableLiters cannot be negative");
        }
    }

    public static Inventory create(Integer userId, BigDecimal availableLiters, LocalDate lastRefillDate, LocalDate nextRefillPrediction) {
        return new Inventory(null, userId, availableLiters, lastRefillDate, nextRefillPrediction);
    }

    public Inventory withAvailableLiters(BigDecimal newAvailableLiters) {
        return new Inventory(inventoryId, userId, newAvailableLiters, lastRefillDate, nextRefillPrediction);
    }

    public Inventory withRefillDates(LocalDate newLastRefillDate, LocalDate newNextRefillPrediction) {
        return new Inventory(inventoryId, userId, availableLiters, newLastRefillDate, newNextRefillPrediction);
    }
}

