package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Solicitud para actualizar el inventario hídrico")
public record UpdateInventoryRequest(
        @Schema(description = "Litros disponibles", example = "4200.00")
        @NotNull(message = "availableLiters is required")
        BigDecimal availableLiters,

        @Schema(description = "Fecha de última recarga", example = "2026-11-13")
        LocalDate lastRefillDate,

        @Schema(description = "Predicción de próxima recarga", example = "2026-11-25")
        LocalDate nextRefillPrediction
) {
}
