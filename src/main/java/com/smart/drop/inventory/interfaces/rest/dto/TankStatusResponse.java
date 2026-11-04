package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Estado operacional y cálculo de capacidad y alertas para un tanque de almacenamiento")
public record TankStatusResponse(
        @Schema(description = "Identificador único del tanque", example = "1")
        Integer tankId,

        @Schema(description = "Nombre o identificador del tanque", example = "Tanque Principal Planta 1")
        String tankName,

        @Schema(description = "Capacidad total máxima en litros", example = "2500")
        Integer capacity,

        @Schema(description = "Nivel actual de almacenamiento en litros", example = "1850")
        Integer currentLevel,

        @Schema(description = "Porcentaje de llenado actual (0.0 a 100.0%)", example = "74.0")
        Double percentage,

        @Schema(description = "Estado operacional clasificado (CRITICAL, LOW, NORMAL, FULL)", example = "NORMAL")
        String status,

        @Schema(description = "Indica si se encuentra en nivel bajo de reserva (< 30%)", example = "false")
        Boolean isLowLevelAlert,

        @Schema(description = "Indica si se encuentra en nivel crítico inminente de agotamiento (< 15%)", example = "false")
        Boolean isCriticalAlert,

        @Schema(description = "Marca de tiempo de la evaluación del estado", example = "2026-11-13T23:45:00")
        LocalDateTime evaluatedAt
) {
}
