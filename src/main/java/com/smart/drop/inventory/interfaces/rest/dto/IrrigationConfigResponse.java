package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Configuración del subsistema de riego automatizado")
public record IrrigationConfigResponse(
        @Schema(description = "Identificador de la configuración de riego", example = "1")
        Integer irrigationConfigId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Modo de anulación manual del cronograma de riego", example = "false")
        Boolean manualOverride,

        @Schema(description = "Porcentaje de humedad actual del suelo (0 a 100%)", example = "65")
        Integer soilMoisture,

        @Schema(description = "Hora programada para el próximo ciclo de riego", example = "06:00 AM")
        String irrigationNext
) {
}
