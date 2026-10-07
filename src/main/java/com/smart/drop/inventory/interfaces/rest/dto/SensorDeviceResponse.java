package com.smart.drop.inventory.interfaces.rest.dto;

public record SensorDeviceResponse(
        Integer deviceId,
        Integer userId,
        String name,
        String location,
        String flow,
        String daily,
        Integer battery,
        String status
) {
}
