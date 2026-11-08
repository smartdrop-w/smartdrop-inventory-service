package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Solicitud para modificar la configuración de riego")
public record UpdateIrrigationConfigRequest(
        @Schema(description = "Modo de anulación manual", example = "true")
        Boolean manualOverride,

        @Schema(description = "Porcentaje de humedad del suelo (0 a 100%)", example = "58")
        @Min(value = 0, message = "soilMoisture must be >= 0")
        @Max(value = 100, message = "soilMoisture must be <= 100")
        Integer soilMoisture,

        @Schema(description = "Horario programado para el siguiente riego", example = "05:30 AM")
        String irrigationNext
) {
}
