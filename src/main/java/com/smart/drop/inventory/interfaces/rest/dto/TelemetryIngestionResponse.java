package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Resultado del procesamiento y normalización de telemetría IoT")
public record TelemetryIngestionResponse(
        @Schema(description = "Indica si la telemetría fue procesada con éxito", example = "true")
        boolean processed,

        @Schema(description = "Código de sensor procesado", example = "DEV-US-001")
        String sensorId,

        @Schema(description = "Tipo de sensor normalizado", example = "ULTRASONIC_LEVEL")
        String sensorType,

        @Schema(description = "Valor físico canónico normalizado", example = "80.0")
        double normalizedValue,

        @Schema(description = "Unidad canónica del dominio (PERCENTAGE, CELSIUS, LITERS)", example = "PERCENTAGE")
        String canonicalUnit,

        @Schema(description = "Métrica de negocio asignada", example = "TANK_FILL_LEVEL")
        String metricName,

        @Schema(description = "Identificador del tanque actualizado si aplica", example = "1")
        Integer updatedTankId,

        @Schema(description = "Nuevo nivel del tanque en litros", example = "2000")
        Integer updatedTankLevel,

        @Schema(description = "Identificador del registro de consumo generado si aplica", example = "15")
        Integer registeredConsumptionId,

        @Schema(description = "Mensaje informativo de auditoría de ingesta", example = "Telemetry successfully normalized and registered.")
        String message,

        @Schema(description = "Fecha y hora del procesamiento de telemetría", example = "2026-11-13T23:45:00")
        LocalDateTime processedAt
) {
}
