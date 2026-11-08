package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle del tanque de agua y nivel registrado")
public record TankResponse(
        @Schema(description = "Identificador único del tanque", example = "1")
        Integer tankId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Nombre o denominación del tanque", example = "Tanque Principal Planta 1")
        String name,

        @Schema(description = "Capacidad total en litros", example = "2500")
        Integer capacity,

        @Schema(description = "Nivel actual en litros", example = "1850")
        Integer current
) {
}
