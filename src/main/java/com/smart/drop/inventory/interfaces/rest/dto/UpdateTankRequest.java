package com.smart.drop.inventory.interfaces.rest.dto;

public record UpdateTankRequest(
        String name,
        Integer capacity,
        Integer current
) {
}
