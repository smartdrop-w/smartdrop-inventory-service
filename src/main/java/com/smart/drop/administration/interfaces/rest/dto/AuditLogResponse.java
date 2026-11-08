package com.smart.drop.administration.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Registro de trazabilidad y auditoría de eventos del sistema")
public record AuditLogResponse(
        @Schema(description = "Identificador único del registro de auditoría", example = "501")
        Integer logId,

        @Schema(description = "Identificador del administrador que ejecutó la acción", example = "1")
        Integer adminId,

        @Schema(description = "Acción realizada (TANK_REPLENISH, SENSOR_RECALIBRATE, ROLE_CHANGE)", example = "TANK_REPLENISH")
        String action,

        @Schema(description = "Fecha y hora del evento", example = "2026-11-13T23:40:00")
        LocalDateTime timestamp,

        @Schema(description = "Descripción detallada del evento registrado", example = "Reabastecimiento manual del Tanque 1 a 2500L")
        String description
) {
}
