package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Detalle de consumo hídrico registrado")
public record ConsumptionResponse(
        @Schema(description = "Identificador único de la transacción de consumo", example = "101")
        Integer consumptionId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Volumen de agua consumido en litros", example = "120.50")
        BigDecimal volumeUsed,

        @Schema(description = "Contexto operativo de uso (FERMENTATION_BATCH, CLEANING, IRRIGATION)", example = "FERMENTATION_BATCH_A")
        String usageContext,

        @Schema(description = "Fecha y hora del consumo", example = "2026-11-13T23:30:00")
        LocalDateTime timestamp
) {
}
