package com.smart.drop.inventory.interfaces.rest.dto;

public record IrrigationConfigResponse(
        Integer irrigationConfigId,
        Integer userId,
        Boolean manualOverride,
        Integer soilMoisture,
        String irrigationNext
) {
}
