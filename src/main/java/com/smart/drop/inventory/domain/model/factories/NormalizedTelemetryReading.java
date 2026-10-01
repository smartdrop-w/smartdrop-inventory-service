package com.smart.drop.inventory.domain.model.factories;

import java.time.LocalDateTime;

/**
 * Estructura de dominio canónica y normalizada tras el procesamiento del Factory Method.
 */
public record NormalizedTelemetryReading(
        String sensorId,
        SensorType sensorType,
        double normalizedValue,
        String canonicalUnit,
        String metricName,
        LocalDateTime timestamp
) {
}
