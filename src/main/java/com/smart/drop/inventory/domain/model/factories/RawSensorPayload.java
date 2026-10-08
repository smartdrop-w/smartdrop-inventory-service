package com.smart.drop.inventory.domain.model.factories;

/**
 * Payload crudo recibido directamente desde hardware heterogéneo IoT (ESP32, LoRaWAN, etc.).
 */
public record RawSensorPayload(
        String sensorId,
        String rawType,
        double rawValue,
        String unit,
        long epochTimestamp
) {
}
