package com.smart.drop.inventory.domain.model.entities;

/**
 * Immutable domain entity for sensor devices.
 */
public record SensorDevice(
        Integer deviceId,
        Integer userId,
        String name,
        String location,
        String flow,
        String daily,
        Integer battery,
        String status
) {
    public SensorDevice {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Device name cannot be null or blank");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (battery != null && (battery < 0 || battery > 100)) {
            throw new IllegalArgumentException("Battery must be between 0 and 100");
        }
    }

    public static SensorDevice create(Integer userId, String name, String location, String flow, String daily, Integer battery, String status) {
        return new SensorDevice(null, userId, name, location, flow, daily, battery, status);
    }
}
