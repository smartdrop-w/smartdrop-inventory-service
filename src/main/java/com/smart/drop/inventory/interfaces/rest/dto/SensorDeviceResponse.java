package com.smart.drop.inventory.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ficha de telemetría y estado operativo del sensor IoT")
public record SensorDeviceResponse(
        @Schema(description = "Identificador único del dispositivo", example = "1")
        Integer deviceId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Nombre del sensor", example = "Sonda Nivel Tanque 1")
        String name,

        @Schema(description = "Ubicación de instalación", example = "Área de Fermentación - Planta Principal")
        String location,

        @Schema(description = "Lectura de caudal o flujo actual", example = "4.2 L/min")
        String flow,

        @Schema(description = "Consumo diario medido", example = "450.0 L")
        String daily,

        @Schema(description = "Porcentaje de batería del dispositivo IoT", example = "92")
        Integer battery,

        @Schema(description = "Estado operacional (ONLINE, OFFLINE, WARNING)", example = "ONLINE")
        String status
) {
}
