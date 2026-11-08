package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Solicitud para registrar un inventario hídrico inicial")
public record CreateInventoryRequest(
        @Schema(description = "Identificador del usuario propietario", example = "2")
        @NotNull(message = "userId is required")
        Integer userId,

        @Schema(description = "Volumen disponible actual en litros", example = "3500.00")
        @NotNull(message = "availableLiters is required")
        BigDecimal availableLiters,

        @Schema(description = "Fecha de última recarga", example = "2026-11-10")
        LocalDate lastRefillDate,

        @Schema(description = "Predicción de próxima recarga sugerida", example = "2026-11-20")
        LocalDate nextRefillPrediction
) {
}
