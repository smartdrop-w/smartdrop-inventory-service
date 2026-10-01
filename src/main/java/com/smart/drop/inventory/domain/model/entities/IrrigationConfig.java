package com.smart.drop.inventory.domain.model.entities;

/**
 * Immutable domain entity for irrigation status and configuration.
 */
public record IrrigationConfig(
        Integer irrigationConfigId,
        Integer userId,
        Boolean manualOverride,
        Integer soilMoisture,
        String irrigationNext
) {
    public IrrigationConfig {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (soilMoisture != null && (soilMoisture < 0 || soilMoisture > 100)) {
            throw new IllegalArgumentException("Soil moisture must be between 0 and 100");
        }
    }

    public static IrrigationConfig create(Integer userId, Boolean manualOverride, Integer soilMoisture, String irrigationNext) {
        return new IrrigationConfig(null, userId, manualOverride, soilMoisture, irrigationNext);
    }
}
