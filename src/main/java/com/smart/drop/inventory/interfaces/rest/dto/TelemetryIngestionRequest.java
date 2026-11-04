package com.smart.drop.inventory.interfaces.rest.dto;

import com.smart.drop.inventory.domain.model.factories.SensorType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud de ingesta de telemetría IoT heterogénea (ultrasónica, térmica, flujo)")
public record TelemetryIngestionRequest(
        @Schema(description = "Identificador del dispositivo de sensado registrado", example = "1")
        Integer deviceId,

        @Schema(description = "Identificador de usuario propietario", example = "2")
        @NotNull(message = "userId is required")
        Integer userId,

        @Schema(description = "Identificador del tanque vinculado para actualización volumétrica", example = "1")
        Integer tankId,

        @Schema(description = "Tipo de sensor soportado por el Factory Method", example = "ULTRASONIC_LEVEL")
        @NotNull(message = "sensorType is required")
        SensorType sensorType,

        @Schema(description = "Código de hardware del sensor emisor", example = "DEV-US-001")
        @NotBlank(message = "sensorId is required")
        String sensorId,

        @Schema(description = "Tipo crudo emitido por el hardware", example = "ULTRASONIC")
        String rawType,

        @Schema(description = "Valor crudo medido (distancia en cm/in, temperatura en F/C, etc.)", example = "40.0")
        double rawValue,

        @Schema(description = "Unidad de medida emitida por el hardware (CM, IN, FAHRENHEIT, CELSIUS)", example = "CM")
        @NotBlank(message = "unit is required")
        String unit,

        @Schema(description = "Timestamp epoch en milisegundos de la captura", example = "1763078400000")
        long epochTimestamp
) {
}
