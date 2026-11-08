package com.smart.drop.administration.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para registrar o actualizar una variable de configuración global")
public record CreateConfigRequest(
        @Schema(description = "Clave canónica del parámetro", example = "TELEMETRY_SAMPLE_INTERVAL_SECONDS")
        @NotBlank(message = "parameterKey is required")
        String parameterKey,

        @Schema(description = "Valor asignado al parámetro", example = "30")
        @NotBlank(message = "parameterValue is required")
        String parameterValue
) {
}
