package com.smart.drop.administration.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Detalle del parámetro de configuración global del sistema")
public record SystemConfigResponse(
        @Schema(description = "Identificador de la configuración", example = "1")
        Integer configId,

        @Schema(description = "Clave canónica del parámetro", example = "TELEMETRY_SAMPLE_INTERVAL_SECONDS")
        String parameterKey,

        @Schema(description = "Valor del parámetro", example = "30")
        String parameterValue,

        @Schema(description = "Fecha de última modificación", example = "2026-11-13T23:40:00")
        LocalDateTime updatedAt
) {
}
