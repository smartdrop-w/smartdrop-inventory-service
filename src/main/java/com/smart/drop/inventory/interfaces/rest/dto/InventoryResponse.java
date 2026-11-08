package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Detalle del inventario de agua disponible y predicción de recarga")
public record InventoryResponse(
        @Schema(description = "Identificador único de inventario", example = "1")
        Integer inventoryId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Litros totales disponibles", example = "3500.00")
        BigDecimal availableLiters,

        @Schema(description = "Fecha de última recarga", example = "2026-11-10")
        LocalDate lastRefillDate,

        @Schema(description = "Predicción de próxima recarga", example = "2026-11-20")
        LocalDate nextRefillPrediction
) {
}
