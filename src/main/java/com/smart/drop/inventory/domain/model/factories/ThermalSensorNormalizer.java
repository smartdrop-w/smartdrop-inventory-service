package com.smart.drop.inventory.domain.model.factories;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Normalizador concreto para sondas térmicas digitales (ej. DS18B20, PT100).
 * Convierte escalas Fahrenheit o Kelvin a grados Celsius estándar.
 */
@Component
public class ThermalSensorNormalizer implements ISensorDataNormalizer {

    @Override
    public NormalizedTelemetryReading normalize(RawSensorPayload rawPayload) {
        double rawTemp = rawPayload.rawValue();
        double celsius;

        if ("F".equalsIgnoreCase(rawPayload.unit()) || "FAHRENHEIT".equalsIgnoreCase(rawPayload.unit())) {
            celsius = (rawTemp - 32.0) * (5.0 / 9.0);
        } else if ("K".equalsIgnoreCase(rawPayload.unit()) || "KELVIN".equalsIgnoreCase(rawPayload.unit())) {
            celsius = rawTemp - 273.15;
        } else {
            celsius = rawTemp; // Ya en Celsius
        }

        LocalDateTime ts = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(rawPayload.epochTimestamp() > 0 ? rawPayload.epochTimestamp() : System.currentTimeMillis()),
                ZoneId.systemDefault()
        );

        return new NormalizedTelemetryReading(
                rawPayload.sensorId(),
                SensorType.THERMAL_PROBE,
                Math.round(celsius * 100.0) / 100.0,
                "CELSIUS",
                "FERMENTATION_TEMPERATURE",
                ts
        );
    }

    @Override
    public SensorType getSupportedType() {
        return SensorType.THERMAL_PROBE;
    }
}
