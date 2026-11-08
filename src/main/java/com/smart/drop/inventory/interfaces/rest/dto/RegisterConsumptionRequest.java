package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Solicitud para registrar un nuevo consumo volumétrico de agua")
public record RegisterConsumptionRequest(
        @Schema(description = "Identificador del usuario responsable", example = "2")
        @NotNull(message = "userId is required")
        Integer userId,

        @Schema(description = "Volumen consumido en litros", example = "120.50")
        @NotNull(message = "volumeUsed is required")
        @DecimalMin(value = "0.01", message = "volumeUsed must be > 0")
        BigDecimal volumeUsed,

        @Schema(description = "Contexto operativo o área de consumo", example = "FERMENTATION_BATCH_A")
        @NotBlank(message = "usageContext is required")
        String usageContext
) {
}
