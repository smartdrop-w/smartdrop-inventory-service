package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para actualizar parámetros de un sensor IoT")
public record UpdateSensorDeviceRequest(
        @Schema(description = "Nombre descriptivo del sensor", example = "Sonda Nivel Tanque 1 - Refactor")
        @NotBlank(message = "name is required")
        String name,

        @Schema(description = "Ubicación física", example = "Tanque 1 - Exterior")
        String location,

        @Schema(description = "Lectura de flujo", example = "3.8 L/min")
        String flow,

        @Schema(description = "Consumo diario medido", example = "420.0 L")
        String daily,

        @Schema(description = "Porcentaje de batería restante", example = "88")
        Integer battery,

        @Schema(description = "Estado operacional (ONLINE, OFFLINE, MAINTENANCE)", example = "ONLINE")
        String status
) {
}
