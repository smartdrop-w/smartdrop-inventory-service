package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para registrar un nuevo sensor IoT en planta")
public record CreateSensorDeviceRequest(
        @Schema(description = "Identificador del usuario", example = "2")
        @NotNull(message = "userId is required")
        Integer userId,

        @Schema(description = "Nombre descriptivo del sensor", example = "Sonda Nivel Tanque 1")
        @NotBlank(message = "name is required")
        String name,

        @Schema(description = "Ubicación física de instalación", example = "Área de Fermentación - Planta Principal")
        String location,

        @Schema(description = "Flujo o lectura inicial reportada", example = "0.0 L/min")
        String flow,

        @Schema(description = "Consumo diario estimado o acumulado", example = "450.0 L")
        String daily,

        @Schema(description = "Nivel de batería restante (0 a 100%)", example = "95")
        Integer battery,

        @Schema(description = "Estado inicial del sensor (ONLINE, OFFLINE, MAINTENANCE)", example = "ONLINE")
        String status
) {
}
