package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para actualizar las características o nivel de un tanque")
public record UpdateTankRequest(
        @Schema(description = "Nombre o denominación del tanque", example = "Tanque Principal Planta 1 - Refactor")
        @NotBlank(message = "name is required")
        String name,

        @Schema(description = "Capacidad nominal en litros", example = "2500")
        @NotNull(message = "capacity is required")
        @Min(value = 1, message = "capacity must be > 0")
        Integer capacity,

        @Schema(description = "Nivel actual en litros", example = "2100")
        Integer current
) {
}
