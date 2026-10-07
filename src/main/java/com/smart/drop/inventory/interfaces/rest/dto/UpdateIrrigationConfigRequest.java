package com.smart.drop.inventory.interfaces.rest.dto;

public record UpdateIrrigationConfigRequest(
        Boolean manualOverride,
        Integer soilMoisture,
        String irrigationNext
) {
}
