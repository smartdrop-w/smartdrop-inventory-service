package com.smart.drop.inventory.domain.model.factories;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Normalizador concreto para sensores ultrasónicos de nivel (ej. HC-SR04, JSN-SR04T).
 * Convierte distancias medidas en centímetros o pulgadas a porcentaje de llenado (0 a 100%).
 */
@Component
public class UltrasonicSensorNormalizer implements ISensorDataNormalizer {

    private static final double DEFAULT_TANK_DEPTH_CM = 200.0;

    @Override
    public NormalizedTelemetryReading normalize(RawSensorPayload rawPayload) {
        double distanceCm = rawPayload.rawValue();

        // Convertir pulgadas a cm si el fabricante lo emite en "IN" o "INCHES"
        if ("IN".equalsIgnoreCase(rawPayload.unit()) || "INCH".equalsIgnoreCase(rawPayload.unit())) {
            distanceCm = distanceCm * 2.54;
        }

        // Cálculo volumétrico: a menor distancia sensor-líquido, mayor nivel en el tanque
        double liquidLevelCm = Math.max(0.0, DEFAULT_TANK_DEPTH_CM - distanceCm);
        double fillPercentage = Math.min(100.0, (liquidLevelCm / DEFAULT_TANK_DEPTH_CM) * 100.0);

        LocalDateTime ts = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(rawPayload.epochTimestamp() > 0 ? rawPayload.epochTimestamp() : System.currentTimeMillis()),
                ZoneId.systemDefault()
        );

        return new NormalizedTelemetryReading(
                rawPayload.sensorId(),
                SensorType.ULTRASONIC_LEVEL,
                Math.round(fillPercentage * 10.0) / 10.0,
                "PERCENTAGE",
                "TANK_FILL_LEVEL",
                ts
        );
    }

    @Override
    public SensorType getSupportedType() {
        return SensorType.ULTRASONIC_LEVEL;
    }
}
