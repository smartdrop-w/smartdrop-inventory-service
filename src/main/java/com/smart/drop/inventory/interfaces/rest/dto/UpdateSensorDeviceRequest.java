package com.smart.drop.inventory.interfaces.rest.dto;

public record UpdateSensorDeviceRequest(
        String name,
        String location,
        String flow,
        String daily,
        Integer battery,
        String status
) {
}
